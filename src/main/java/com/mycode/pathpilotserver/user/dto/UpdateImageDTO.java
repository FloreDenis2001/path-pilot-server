package com.mycode.pathpilotserver.user.dto;

import com.mycode.pathpilotserver.image.models.Image;

public record UpdateImageDTO(Image image,String email) {

}
