package com.mycode.pathpilotserver.utils;

import com.mycode.pathpilotserver.address.dto.AddressDTO;
import com.mycode.pathpilotserver.address.models.Address;
import com.mycode.pathpilotserver.company.dto.CompanyDTO;
import com.mycode.pathpilotserver.company.models.Company;
import com.mycode.pathpilotserver.driver.dto.DriverDTO;
import com.mycode.pathpilotserver.orders.models.Order;
import com.mycode.pathpilotserver.packages.dto.PackageDTO;
import com.mycode.pathpilotserver.packages.dto.PackageDetails;
import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.routes.models.Route;
import com.mycode.pathpilotserver.shipments.dto.ShipmentDTO;
import com.mycode.pathpilotserver.shipments.models.Shipment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mycode.pathpilotserver.city.utils.Utils.getCityByName;


public class Convertor {

    public static ShipmentDTO convertToShipmentDTO(Shipment s) {

        return ShipmentDTO.builder()
                .originName(s.getOriginName())
                .destinationName(s.getDestinationName())
                .originPhone(s.getOriginPhone())
                .destinationPhone(s.getDestinationPhone())
                .origin(AddressDTO.from(s.getOriginAddress()))
                .destination(AddressDTO.from(s.getDestinationAddress()))
                .status(s.getStatus().toString())
                .totalDistance(s.getTotalDistance())
                .build();
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

    public static CompanyDTO convertCompanyToCompanyDTO(Company company) {
        return CompanyDTO.builder()
                .address(AddressDTO.from(company.getAddress()))
                .capital(company.getIncome())
                .email(company.getEmail())
                .name(company.getName())
                .phone(company.getPhone())
                .registrationNumber(company.getRegistrationNumber())
                .industry(company.getIndustry())
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
        order.setLength(pack.getLength());
        order.setAwb(pack.getAwb());

        return order;

    }


    public static Address convertAddressDTOToAddress(AddressDTO addressDTO) {
        return Address.builder()
                .cityDetails(getCityByName(addressDTO.city()))
                .postalCode(addressDTO.postalCode())
                .street(addressDTO.street())
                .streetNumber(addressDTO.streetNumber())
                .build();
    }


}
