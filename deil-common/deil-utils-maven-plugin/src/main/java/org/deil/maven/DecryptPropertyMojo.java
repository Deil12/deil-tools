package org.deil.maven;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;

//@Mojo(name = "decrypt", defaultPhase = LifecyclePhase.NONE, requiresProject = false)
@Mojo(name = "decrypt", defaultPhase = LifecyclePhase.NONE, requiresProject = true, threadSafe = true)
public class DecryptPropertyMojo extends AbstractJasyptMojo {

    /**
     * Sets the password to be used for decryption.
     */
    @Parameter(required = true, defaultValue = "${jasyptPassword}")
    private String jasyptPassword;

    /**
     * The encrypted value to be decrypted.
     */
    @Parameter(required = true, defaultValue = "${decryptValue}")
    private String decryptValue;

    @Override
    public void execute() {
        final PooledPBEStringEncryptor encryptor = buildEncryptor();
        encryptor.setPassword(jasyptPassword);

        final String decryptedValue = PropertyValueEncryptionUtils.decrypt(decryptValue, encryptor);
        getLog().info("\n[INFO] ------------------------------[ result ]-------------------------------\n\n\033[0;32m" + decryptedValue + "\033[0m\n");
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
     * The encrypted value to be decrypted.
     *
     * @param decryptValue the value to be decrypted.
     */
    public void setDecryptValue(String decryptValue) {
        this.decryptValue = decryptValue;
    }
}
