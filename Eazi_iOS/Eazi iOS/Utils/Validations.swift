//
//  Validations.swift
//  BREATHE Easy
//
//  Created by IdeaMac on 24/01/18.
//  Copyright © 2018 IdeaMac. All rights reserved.
//

import Foundation

class Validations {
    
    static func isValidEmail(string:String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: string)
    }
    
    static func isStringEmpty(string:String) -> Bool {
        if(string.count == 0 || string == "") {
            return true
        }
        return false
    }
    
    static func isPasswordValid(_ password : String) -> Bool{
        let passwordTest = NSPredicate(format: "SELF MATCHES %@", "^(?=.*[a-z])(?=.*[$@$#!%*?&])[A-Za-z\\d$@$#!%*?&]{8,}")
        return passwordTest.evaluate(with: password)
    }
    
    static func isPhoneNumber(_ phoneNum : String) -> Bool{
        let passwordTest = NSPredicate(format: "SELF MATCHES %@", "^(?=.*[a-z])(?=.*[$@$#!%*?&])[A-Za-z\\d$@$#!%*?&]{8,}")
        return passwordTest.evaluate(with: phoneNum)
    }
}
