package com.mydomain.vo;

import java.io.Serializable;

public class Code implements Serializable{
 private String code;
 private String codeOption;
 private String codeValue;
 private String explagin;
 
public Code() {
	super();
}
@Override
public String toString() {
	return "Code [code=" + code + ", codeOption=" + codeOption + ", codeValue=" + codeValue + ", explagin=" + explagin
			+ "]";
}
public Code(String code, String codeOption, String codeValue, String explagin) {
	super();
	this.code = code;
	this.codeOption = codeOption;
	this.codeValue = codeValue;
	this.explagin = explagin;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((code == null) ? 0 : code.hashCode());
	result = prime * result + ((codeOption == null) ? 0 : codeOption.hashCode());
	result = prime * result + ((codeValue == null) ? 0 : codeValue.hashCode());
	result = prime * result + ((explagin == null) ? 0 : explagin.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Code other = (Code) obj;
	if (code == null) {
		if (other.code != null)
			return false;
	} else if (!code.equals(other.code))
		return false;
	if (codeOption == null) {
		if (other.codeOption != null)
			return false;
	} else if (!codeOption.equals(other.codeOption))
		return false;
	if (codeValue == null) {
		if (other.codeValue != null)
			return false;
	} else if (!codeValue.equals(other.codeValue))
		return false;
	if (explagin == null) {
		if (other.explagin != null)
			return false;
	} else if (!explagin.equals(other.explagin))
		return false;
	return true;
}
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public String getCodeOption() {
	return codeOption;
}
public void setCodeOption(String codeOption) {
	this.codeOption = codeOption;
}
public String getCodeValue() {
	return codeValue;
}
public void setCodeValue(String codeValue) {
	this.codeValue = codeValue;
}
public String getExplagin() {
	return explagin;
}
public void setExplagin(String explagin) {
	this.explagin = explagin;
}
}
