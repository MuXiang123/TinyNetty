package io.netty.example.study.common.auth;

import io.netty.example.study.common.OperationResult;
import lombok.Data;

/**
 * 授权结果
 */
@Data
public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;

}
