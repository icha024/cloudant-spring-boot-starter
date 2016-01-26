package com.clianz.cloudant.basedocument;

import com.google.gson.annotations.SerializedName;

/**
 * A convenient base class for Cloudant object to generate id and rev field.
 */
public abstract class CloudantDoc {

	@SerializedName("_id")
	private String id;

	@SerializedName("_rev")
	private String rev;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}
}
