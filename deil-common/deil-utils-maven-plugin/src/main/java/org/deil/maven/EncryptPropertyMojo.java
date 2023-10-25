package org.deil.maven;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;

@Mojo(name = "encrypt", defaultPhase = LifecyclePhase.NONE, requiresProject = false)
public class EncryptPropertyMojo extends AbstractJasyptMojo {

    /**
     * Sets the password to be used for decryption.
     */
    @Parameter(required = true, defaultValue = "${jasyptPassword}")
    private String jasyptPassword;

    /**
     * The decrypted value to be encrypted.
     */
    @Parameter(required = true, defaultValue = "${encryptValue}")
    private String encryptValue;

    @Override
    public void execute() {
        final PooledPBEStringEncryptor encryptor = buildEncryptor();
        encryptor.setPassword(jasyptPassword);

        final String encryptedValue = PropertyValueEncryptionUtils.encrypt(encryptValue, encryptor);
        getLog().info("\n[INFO] ------------------------------[ result ]-------------------------------\n\n\033[0;32m" + encryptedValue + "\033[0m\n");
    }

    /**
     * Sets the password to be used for decryption.
     *
     * @param jasyptPassword the password to be used.
     */
    public void setJasyptPassword(String jasyptPassword) {
        this.jasyptPassword = jasyptPassword;
    }

    /**
     * The decrypted value to be encrypted.
     *
     * @param encryptValue the value to be encrypted.
     */
    public void setEncryptValue(String encryptValue) {
        this.encryptValue = encryptValue;
    }
}
