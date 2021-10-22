package eu.builderscoffee.api.common.redisson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Configuration(value = "redis")
public final class RedisCredentials {

    private String clientName = "",ip = "",password = "";
    private int port;

    @JsonIgnore
    public boolean isAuth() {
        return password !=null;
    }

    @JsonIgnore
    public String toRedisUrl() {
        return "redis://" + ip + ":" + port;
    }
}
