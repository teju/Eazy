//
//  Utils.swift
//  Eazi iOS
//
//  Created by Tejaswini on 27/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import Foundation
import UIKit
import Contacts
import CoreData
import CoreTelephony
import CallKit
class Utils {
    static var gradientLayer: CAGradientLayer!

    static func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if ((cString.characters.count) != 6) {
            return UIColor.gray
        }
        
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    
    static func retrieveContactsWithStore(store: CNContactStore) -> [CNContact]
    {
        
        var objects  = [CNContact]()

        let keysToFetch = [CNContactFormatter.descriptorForRequiredKeys(for: .fullName), CNContactPhoneNumbersKey,CNContactImageDataKey, CNContactEmailAddressesKey] as [Any]
        let request = CNContactFetchRequest(keysToFetch: keysToFetch as! [CNKeyDescriptor])
        var cnContacts = [CNContact]()
        do {
            try store.enumerateContacts(with: request){
                (contact, cursor) -> Void in
                if (!contact.phoneNumbers.isEmpty) {
                }
                
                if contact.isKeyAvailable(CNContactImageDataKey) {
                    if let contactImageData = contact.imageData {
                        print(UIImage(data: contactImageData)) // Print the image set on the contact
                    }
                } else {
                    // No Image available
                    
                }
                if (!contact.emailAddresses.isEmpty) {
                }
                cnContacts.append(contact)
                objects = cnContacts
            }
        } catch let error {
            NSLog("Fetch contact error: \(error)")
        }
        
        NSLog(">>>> Contact list:")
        for contact in cnContacts {
            let fullName = CNContactFormatter.string(from: contact, style: .fullName) ?? "No Name"

            NSLog("Name \(fullName):")
        }
        return objects
    }
    
  
    
  static func  getContacts() -> CNContactStore {
        let store = CNContactStore()
        
        switch CNContactStore.authorizationStatus(for: .contacts){
        case .authorized:
            return store
            
        // This is the method we will create
        case .notDetermined:
            store.requestAccess(for: .contacts){succeeded, err in
                guard err == nil && succeeded else{
                    return
                }
            }
            break
        default:
            print("Not handled")
        }
        return store

    }
    
    static func createGradientLayer(view : UIView,startcolor:UIColor, endColor:UIColor) {
        
        
        gradientLayer = CAGradientLayer()
        gradientLayer.frame = view.bounds
        
        gradientLayer.colors = [startcolor.cgColor, endColor.cgColor]
        gradientLayer.locations = [0.0, 0.35]
        
        gradientLayer.startPoint = CGPoint(x: 0.0, y: 0.5)
        gradientLayer.endPoint = CGPoint(x: 1.0, y: 0.5)
    }
    
    static func addLine(button:UIView,color:CGColor)//  need to move to one place
    {
        // let count =   viewlbl.text?.characters.count
        
        let length = button.bounds.size.width
        let x  =  button.bounds.origin.x
        let y = button.bounds.origin.y + button.bounds.height
        let path = UIBezierPath()
        path.move(to: CGPoint(x: x, y: y))
        path.addLine(to: CGPoint(x: x + length , y:y))
        //design path in layer
        let lineLayer = CAShapeLayer()
        lineLayer.path = path.cgPath
        lineLayer.strokeColor = color
        lineLayer.lineWidth = 1.0
        lineLayer.fillColor  = UIColor.clear.cgColor
        button.layer.insertSublayer(lineLayer, at: 0)
    }
}
