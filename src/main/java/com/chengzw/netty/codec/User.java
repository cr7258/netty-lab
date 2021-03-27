package com.chengzw.netty.codec;

import java.io.Serializable;


/**
 * 实体类
 * @author 程治玮
 * @since 2021/3/25 9:54 下午
 */
public class User implements Serializable {
	
	private int id;
	private String name;

	public User(){}

	public User(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
