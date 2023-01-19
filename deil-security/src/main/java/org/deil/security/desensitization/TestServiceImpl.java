package org.deil.security.desensitization;

import lombok.extern.slf4j.Slf4j;
import org.deil.security.desensitization.encryption.annotation.EncryptMethod;
import org.springframework.stereotype.Service;

/**
 * 测试 service 层
 *
 * @DATE 2023/01/02
 * @CODE Deil
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @EncryptMethod("num")
    @Override
    public String test2(String num) {
        return num;
    }

}
