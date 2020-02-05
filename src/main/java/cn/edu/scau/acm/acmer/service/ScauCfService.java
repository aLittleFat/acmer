package cn.edu.scau.acm.acmer.service;

public interface ScauCfService {
    String login();
    String sendCfVerifyCode(String cfHandle);
}
