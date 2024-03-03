package blog.sirico.Blog;

import com.password4j.*;
import com.password4j.types.Bcrypt;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = hashPassword(password);
    }

    public String hashPassword(String password) {
        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

        Hash hash = Password.hash(password)
                .addPepper("shared-secret")
                .with(bcrypt);

        return hash.getResult();
    }

    public boolean checkPassword(String password) {
        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);
        boolean verified = Password.check(password, this.password).addPepper("shared-secret") .with(bcrypt);
        return verified;
    }

    public String getUsername() {
        return username;
    }

}
