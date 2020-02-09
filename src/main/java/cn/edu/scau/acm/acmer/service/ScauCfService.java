package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;

public interface ScauCfService {
    String login();
    MyResponseEntity<Void> sendCfVerifyCode(String cfHandle);
}
