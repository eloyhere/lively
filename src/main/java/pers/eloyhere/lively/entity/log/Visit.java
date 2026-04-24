package pers.eloyhere.lively.entity.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pers.eloyhere.lively.entity.BaseEntity;

import java.util.TreeMap;

@Entity
@Table(name = "visit")
public class Visit extends BaseEntity {

    @Column(name = "ip", nullable = false)
    private String ip;

    @Column(name = "device", nullable = false)
    private String device;

    @Column(name = "platform", nullable = false)
    private String platform;

    @Column(name = "system", nullable = false)
    private String system;

    @Column(name = "os", nullable = false)
    private String os;

    @Column(name = "client", nullable = false)
    private String client;

    public Visit() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public TreeMap<String, Object> properties() {
        TreeMap<String, Object> map = super.properties();
        map.put("ip", this.getIp());
        map.put("device", this.getDevice());
        map.put("platform", this.getPlatform());
        map.put("system", this.getSystem());
        map.put("os", this.getOs());
        map.put("client", this.getClient());
        return map;
    }
}