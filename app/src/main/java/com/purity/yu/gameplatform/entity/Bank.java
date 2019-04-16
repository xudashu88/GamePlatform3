package com.purity.yu.gameplatform.entity;

import java.io.Serializable;

public class Bank implements Serializable {
    public int id;
    public String bank_username;//开户人名称
    public String bank_account;//卡号
    public String bank_branch;//所在支行
    public String bank_name;//那个行
    public String mobile;//每个银行卡对应一个手机号码

    public Bank() {
    }

    public Bank(int id, String bank_username, String bank_account, String bank_name, String bank_branch) {
        this.id = id;
        this.bank_username = bank_username;
        this.bank_account = bank_account;
        this.bank_name = bank_name;
        this.bank_branch = bank_branch;
    }
}
