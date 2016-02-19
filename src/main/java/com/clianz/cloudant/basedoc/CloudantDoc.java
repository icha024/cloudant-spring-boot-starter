package com.clianz.cloudant.basedoc;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * A convenient base class for Cloudant object to generate id and rev field.
 */
@Data
public abstract class CloudantDoc {

	@SerializedName("_id")
	private String id;

	@SerializedName("_rev")
	private String rev;
}
