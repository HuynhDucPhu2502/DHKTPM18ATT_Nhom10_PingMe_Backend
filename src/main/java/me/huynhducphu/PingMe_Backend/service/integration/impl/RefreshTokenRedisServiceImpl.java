package me.huynhducphu.PingMe_Backend.service.integration.impl;

import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.auth.SessionMetaRequest;
import me.huynhducphu.PingMe_Backend.dto.response.auth.UserDeviceMetaResponse;
import me.huynhducphu.PingMe_Backend.model.common.DeviceMeta;
import me.huynhducphu.PingMe_Backend.service.integration.RefreshTokenRedisService;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Admin 8/16/2025
 **/
@Service
@RequiredArgsConstructor
public class RefreshTokenRedisServiceImpl implements RefreshTokenRedisService {

    private final RedisTemplate<String, DeviceMeta> redisSessionMetaTemplate;
    private final ModelMapper modelMapper;

    @Override
    public void saveRefreshToken(
            String token, String userId,
            SessionMetaRequest sessionMetaRequest, Duration expire
    ) {
        String sessionId = buildKey(token, userId);

        DeviceMeta deviceMeta = new DeviceMeta(
                sessionId,
                sessionMetaRequest.getDeviceType(),
                sessionMetaRequest.getBrowser(),
                sessionMetaRequest.getOs(),
                Instant.now().toString()
        );

        redisSessionMetaTemplate.opsForValue().set(sessionId, deviceMeta, expire);
    }

    @Override
    public List<UserDeviceMetaResponse> getAllDeviceMetas(String userId, String currentRefreshToken) {
        String keyPattern = "auth::refresh_token:" + userId + ":*";
        Set<String> keys = redisSessionMetaTemplate.keys(keyPattern);

        if (keys == null || keys.isEmpty()) return Collections.emptyList();
        String currentTokenHash = DigestUtils.sha256Hex(currentRefreshToken);

        List<UserDeviceMetaResponse> sessionMetas = new ArrayList<>();
        for (String key : keys) {
            DeviceMeta meta = redisSessionMetaTemplate.opsForValue().get(key);
            if (meta == null) continue;

            String keyHash = key.substring(key.lastIndexOf(":") + 1);
            boolean isCurrent = currentTokenHash.equals(keyHash);

            var sessionMetaResponse = modelMapper.map(meta, UserDeviceMetaResponse.class);
            sessionMetaResponse.setCurrent(isCurrent);

            sessionMetas.add(sessionMetaResponse);
        }
        return sessionMetas;
    }

    @Override
    public void deleteRefreshToken(String token, String userId) {
        redisSessionMetaTemplate.delete(buildKey(token, userId));
    }

    @Override
    public void deleteRefreshToken(String key) {
        redisSessionMetaTemplate.delete(key);
    }

    // =====================================
    // Utilities methods
    // =====================================
    @Override
    public boolean validateToken(String token, String userId) {
        return redisSessionMetaTemplate.hasKey(buildKey(token, userId));
    }

    private String buildKey(String token, String userId) {
        return "auth::refresh_token:" + userId + ":" + DigestUtils.sha256Hex(token);
    }


}
