package com.kunlun.firmwaresystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.ArrayList;

public class Menu {
    int shows;
    int id;
    int pid;
    String type;
    String    title;
    String  name;
    String    path;
    String icon;
    String    menu_type;
    String url;
    String    component;
    String  keepalive;
    String    extend;
    String remark;
    int    weigh;
    String status;
    long   update_time;
    long create_time;
    @TableField(exist=false)
    ArrayList<Menu> children;
    public Menu(){

    }
    public Menu(int id,
            int pid,String type,
            String    title,
            String  name,
            String    path,
            String icon,
            String    menu_type,
            String url,
            String    component,
            String  keepalive,
            String    extend,
            String remark,
            int    weigh,
            String status){
            this.component=component;
            this.extend=extend;
            this.icon=icon;
            this.id=id;
            this.path=path;
            this.pid=pid;
            this.keepalive=keepalive;
            this.remark=remark;
            this.status=status;
            this.weigh=weigh;
            this.name=name;
            this.title=title;
            this.type=type;
            this.menu_type=menu_type;
            this.url=url;
            create_time=System.currentTimeMillis()/1000;
            update_time=System.currentTimeMillis()/1000;
    }
    public void addMenu(Menu menu){
        if(children==null){
            setChildren(new ArrayList<Menu>());
            children.add(menu);
        }else
            children.add(menu);
    }

    public int getShows() {
        return shows;
    }

    public void setShows(int shows) {
        this.shows = shows;
    }

    public void setChildren(ArrayList<Menu> children) {
        this.children = children;
    }

    public ArrayList<Menu> getChildren() {
        return children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(String keepalive) {
        this.keepalive = keepalive;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getWeigh() {
        return weigh;
    }

    public void setWeigh(int weigh) {
        this.weigh = weigh;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", pid=" + pid +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", icon='" + icon + '\'' +
                ", menu_type='" + menu_type + '\'' +
                ", url='" + url + '\'' +
                ", component='" + component + '\'' +
                ", keepalive=" + keepalive +
                ", extend='" + extend + '\'' +
                ", remark='" + remark + '\'' +
                ", weigh=" + weigh +
                ", status='" + status + '\'' +
                ", update_time=" + update_time +
                ", create_time=" + create_time +
                ", children=" + children +
                '}';
    }
}
