//
//  AppConstants.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import Foundation

class AppConstants {
    
    // MARK: - Utility
    static let alertTitle:String  = "BREATHE Easy"
    static let userLoggedIn = "userLoggedIn"
    static let profileImageBase64 = "profileImageBase64"
    static let profileimageName = "Profile.jpg"
    static let timeoutValue  = 60
    enum AlertMessage:String {
        case notRegistered  = "You must create an account to access this feature. Click Yes to proceed."
        case pwdNotMatching = "Password & confirm password are not matching"
        case emailInvalid   = "Invalid email!"
        case userNotRegistered  = "You are not registered with us. Please create an account"
        case passwordNotMatching  = "Password not matching. Please try again"
        case genericError  = "Unexpected error occured"
        case dobText  = "Please enter your date of birth"
        case textMessageTxtMissing   = "Please enter Text Message"
        case commentsTxtMissing   = "Please enter additional features required"
        case enterEmail  = "Please enter email address"
        case enterFName  = "Please enter name!"
        case enterLName  = "Please enter last name!"
        case enterPNumb  = "Please enter phone number!"
        case entervalidPNumb  = "Please enter valid phone number"
        case enterPassword  = "Please enter password"
        case enterCompany  = "Please enter Company Name"
        case enterCompanyAddress  = "Please enter Company Address"
        
        case enterCurrPwd   = "Please enter current password"
        case enterNewPwd    = "Please enter new password"
        case enterConfPwd   = "Please enter confirm password"
        case pwdMismatch    = "New password and confirm password are not matching"
        case enterDob  = "Please enter DOB"
        case enterGeder  = "Please select gender"
        case validEmail  = "Please enter valid email address"
        case validdob  = "Please enter valid Date of birth"
        case relationship  = "Please enter Relationship"
        case emailExists  = "You are already registered with email Id. Please enter a different emai Id"
        
        case validPassword  = "Password must contain a number, a special character, an upper case alphabet, a lower case alphabet and has to be 8 characters in length"
        case passwordchangeSuccess = "Your password has been changed successfully! Thank you."
        case logoutMessage   = "Are you sure you want to logout?"
        case noInternet   = "Please check your internet connection"
        case noalerts   =  "There are no incidents reports for this country from last two weeks"
        case noReports   =  "There are no reports for this category. Please try other category or try again later"
        case successSubscription   =  "Successfully updated the Subscription"
        case countryDetailsNotFound   =  "No Country details found"
        case sosMessgae = "Sos Message"
        case cannotSendSms = "cannot Send Sms"
        case emergencyContactMno = "Enter Mobile number with Country Code"
        case PurposevisitValidation = "Please select the purpose of visit"
        case removeEmergencyContacts = "EmergencyContacts Deleted Successfully"
        case passwordChange = "Password changed Successfully"
        case notificationStatus = "Notification status changed Successfully"
        case requestPassword = "Password has been sent to your mail id."
        case cannotAccessReports = "Please subscribe to access this feature"
        case subscribeCountry = "Please subscribe to subscribe more than three countries"
        case paymentFailer = "Transaction failed. Please try again"
        
    }
    
   
    enum Storyboards:String {
        case Controllers    = "Controllers"
        case Main           = "Main"
        case story          = "story"
    }
    
    enum userDefaults:String {
        case user_data    = "user_data"
        case user_pass    = "user_pass"

        case isLoggedIn   = "isLoggedIn"
        case subscribedCountries = "subscribedCountries"
        case server_date   = "server_date"
        case myContacts    = "myContacts"
        case recent_alerts = "recent_alerts"
        case Notification_status = "Notification_status"
        case token =       "token"
        case subscriptionDate =       "subscriptionDate"
        case recentAlertDate =       "recentAlertDate"
        case trackLocation =       "trackLocation"
        case accountType =       "AccountType"
        
    }
   
   
    
}
