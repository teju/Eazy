//
//  TMNetworkConstants.swift
//  Telemedicine
//
//  Created by Ameer on 12/8/16.
//  Copyright © 2016 UST. All rights reserved.
//

import Foundation

class NetworkConstants {
    static let rootURL = "http://45.79.138.18:8080/"
    
}

enum ApiMethod
{
    static let post     = "POST"
    static let get      = "GET"
    static let put      = "PUT"

}

enum WebserviceMethods: String {
    
    case register                = "register"
    case confirm                 = "confirm"
    case country                 = "country"

}

