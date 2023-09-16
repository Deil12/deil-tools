package org.deil.utils.signature;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SignatureManager {

    public Map<String, String> packSigns(Map<String, String> data) {
        return SignatureSuper.packSigns(data);
    }

}
