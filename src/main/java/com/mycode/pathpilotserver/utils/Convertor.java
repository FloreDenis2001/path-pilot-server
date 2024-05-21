package com.mycode.pathpilotserver.utils;

import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.image.models.Image;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.dto.PackageDetails;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.shipments.dto.ShipmentDTO;
import com.mycode.pathpilotserver.shipments.models.Shipment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Convertor {



    public static ShipmentDTO convertToShipmentDTO(Shipment s) {

        ShipmentDTO shipmentDTO = ShipmentDTO.builder()
                .originName(s.getOriginName())
                .destinationName(s.getDestinationName())
                .originPhone(s.getOriginPhone())
                .destinationPhone(s.getDestinationPhone())
                .origin(s.getOriginAddress())
                .destination(s.getDestinationAddress())
                .status(s.getStatus().toString())
                .totalDistance(s.getTotalDistance())
                .build();

        return shipmentDTO;
    }


    public static List<PackageDTO> convertToPackageDTO(List<Package> packages) {

        List<PackageDTO> packageDTOS = new ArrayList<>();
        for (Package p : packages) {
            PackageDetails packageDetails = PackageDetails.builder()
                    .totalAmount(p.getTotalAmount())
                    .weight(p.getWeight())
                    .height(p.getHeight())
                    .length(p.getLength())
                    .width(p.getWidth())
                    .deliveryDescription(p.getDeliveryDescription())
                    .build();

            ShipmentDTO shipmentDTO = convertToShipmentDTO(p.getShipment());

            PackageDTO packageDTO = PackageDTO.builder()
                    .customerEmail(p.getCustomer().getEmail())
                    .awb(p.getAwb())
                    .status(p.getStatus())
                    .packageDetails(packageDetails)
                    .shipmentDTO(shipmentDTO)
                    .build();

            packageDTOS.add(packageDTO);
        }

        return packageDTOS;
    }

    public static Image convertMultipartFileToImage(MultipartFile file) {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        try {
            image.setData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not convert MultipartFile to Image");
        }
        return image;
    }
    public static CompanyDTO convertCompanyToCompanyDTO(Company company) {
        return CompanyDTO.builder()
                .address(company.getAddress())
                .capital(company.getCapital())
                .email(company.getEmail())
                .name(company.getName())
                .phone(company.getPhone())
                .registrationNumber(company.getRegistrationNumber())
                .industry(company.getIndustry())
                .address(company.getAddress())
                .website(company.getWebsite())
                .build();
    }

    public static Order convertPackageToOrder(Package pack) {
        Order order = new Order();
        order.setCustomer(pack.getCustomer());
        order.setShipment(pack.getShipment());
        order.setDeliveryDescription(pack.getDeliveryDescription());
        order.setHeight(pack.getHeight());
        order.setOrderDate(pack.getOrderDate());
        order.setTotalAmount(pack.getTotalAmount());
        order.setWeight(pack.getWeight());
        order.setWidth(pack.getWidth());

        return order;

    }
}
