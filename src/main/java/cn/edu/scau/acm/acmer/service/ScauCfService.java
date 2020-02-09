package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;

public interface ScauCfService {
    String login();
    void sendCfVerifyCode(String cfHandle) throws Exception;
}
