package com.example.math;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@ShellComponent
public class test {
	@ShellMethod("Add two integers together.")
	public int add(int a, int b) {
		return a + b;
	}

	@ShellMethod("http")
	public void http() {

		// POST http://webapi.xfyun.cn/v1/service/v1/ocr/handwriting HTTP/1.1
		// Content-Type:application/x-www-form-urlencoded; charset=utf-8
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		List<Charset> acceptableCharsets = new ArrayList<>();
		acceptableCharsets.add(Charset.forName("UTF-8"));
		headers.setAcceptCharset(acceptableCharsets);
		
		
		String appId = "5ba49475";
		String apiKey = "a8710e3b2e1872a325ee6fc95e4bb90b";
		String curTime = new Date().getTime() / 1000 + "";
		String json = "{\"language\": \"en\",\n" + "    \"location\": \"false\"}";
		
		String param = Base64.getEncoder().encodeToString(json.getBytes());
		String temp = apiKey + curTime + param;
		String checkSum = DigestUtils.md5DigestAsHex(temp.getBytes());

		System.out.println(apiKey);
		System.out.println(curTime);
		System.out.println(json);
		System.out.println(param);
		System.out.println(temp);
		System.out.println(checkSum);

		headers.set("X-Appid", appId);
		headers.set("X-CurTime", curTime);
		headers.set("X-Param", param);
		headers.set("X-CheckSum", checkSum);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params,
				headers);
		String image = Base64Utils.ImageToBase64ByLocal("/home/lihao/data/1438877737.jpg");
		requestEntity.getBody().add("image", image);
		// 执行HTTP请求
		ResponseEntity<String> response = client.exchange("http://webapi.xfyun.cn/v1/service/v1/ocr/handwriting",
				HttpMethod.POST, requestEntity, String.class);
		String a = response.getBody();
		System.out.println(a);
	}
}
