package com.mycode.pathpilotserver.intercom.maps;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MapClientfgn", url = "${client.server.uri}")
public interface MapClient {
}
