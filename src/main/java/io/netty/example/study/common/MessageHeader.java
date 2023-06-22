package io.netty.example.study.common;

import lombok.Data;

@Data
public class MessageHeader {
    //示范version
    private int version = 1;
    private int opCode;
    private long streamId;

}
