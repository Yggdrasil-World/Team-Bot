package de.overcraft.util.userinfo;

import de.overcraft.exceptions.InvalidParameterFormatException;

import java.io.Reader;
import java.util.Map;
import java.util.Set;

public interface UserInfoPipeline {
    String convert(Set<Map.Entry<Long, UserInfo>> userInfos);
    Set<Map.Entry<Long, UserInfo>> convert(Reader userInfo);
}
