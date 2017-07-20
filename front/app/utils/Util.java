package utils;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;


public class Util {

	public static JsonNode createResponse(Object response, boolean ok) {

		return Json.mapper().convertValue(response.toString(), JsonNode.class);
	}

}
