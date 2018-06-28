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
import MBProgressHUD

class Utils {
    static var gradientLayer: CAGradientLayer!
    static var cview : UIView?
    private static var hud:MBProgressHUD!

    static func setupNotificationReminder(to_user : String) {
        var title:String = "New Message Arrived "
        
        let date = Date()
        
        // create a corresponding local notification
        let notification = UILocalNotification()
        
        let dict:NSDictionary = ["to_user" : to_user]
        notification.userInfo = dict as! [String : String]
        notification.alertBody = "\(title)"
        notification.alertAction = "Open"
        notification.fireDate = Date()
        notification.repeatInterval = .year  // Can be used to repeat the notification
        notification.soundName = UILocalNotificationDefaultSoundName
        UIApplication.shared.scheduleLocalNotification(notification)
    }
    class func showAlert(message:String, viewController:UIViewController, yesCallback:@escaping (_ action:UIAlertAction) -> Void, noCallback:@escaping (_ action:UIAlertAction) -> Void) {
        
        let alert = UIAlertController(title: AppConstants.alertTitle, message: message, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: { (action) in
            yesCallback(action)
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.default, handler: { (action) in
            noCallback(action)
        }))
        
        viewController.present(alert, animated: true, completion: nil)
    }
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
        
        for contact in cnContacts {
            let fullName = CNContactFormatter.string(from: contact, style: .fullName) ?? "No Name"

        }
        // sort by name given
        let result = objects.sorted(by: {
            (firt: CNContact, second: CNContact) -> Bool in firt.givenName < second.givenName
        })
        return result
    }

    static func getContactName(to_user: String) -> String {
        var objects  = [CNContact]()
        var user_name = ""
        
        let store = Utils.getContacts()
        objects = Utils.retrieveContactsWithStore(store: store)
        for index in 0 ... objects.count - 1 {
            let contact = objects[index]
            let actualNumber = contact.phoneNumbers.first?.value as? CNPhoneNumber
        
            var string = actualNumber?.stringValue
            string = string?.replacingOccurrences(of: " ", with: "")
            print("actualNumber123 \(string) to_user \(to_user)")

            if to_user.range(of:string!) != nil {
                print("exists")
                let fullName = CNContactFormatter.string(from: contact, style: .fullName) ?? "No Name"
                return fullName
                break
            }
        }
        return user_name
    }
    
    static func htmlToString(mstring : String) -> String {
        let htmlStringData = mstring.data(using: String.Encoding.utf8)!
        
        let options =  try? NSAttributedString(data: htmlStringData, options: [NSAttributedString.DocumentReadingOptionKey.documentType: NSAttributedString.DocumentType.html], documentAttributes: nil)
        
        let string = options?.string
        return string!
    }
  
    static func getCurrentDate() -> String{
        let formatter = DateFormatter()
        // initially set the format based on your datepicker date / server String
        formatter.dateFormat = "dd MMM yyyy hh:mm:ss a"
    
        let myString = formatter.string(from: Date()) // string purpose I add here
        print(myString)
        return myString
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
    static func placeHolderText(textFiled :UITextField , color : UIColor, text : String){
        textFiled.attributedPlaceholder = NSAttributedString(string: text,
                                                             attributes: [NSAttributedStringKey.foregroundColor: color])
    }
    class func showProgressHudToView(view:UIView, message:String) {
        
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        if !appDelegate.loaderFlag {
            
            hud = MBProgressHUD.showAdded(to: view, animated: true)
            hud.label.text = message
            hud.minSize = CGSize.init(width: 150, height: 100)
        }
        else{
            appDelegate.loaderFlag = false
        }
    }
    
    class func isProgressHudToView(view:UIView) {
        if(hud != nil ) {
            hud.hide(animated: true)
            
        }
    }
    class alert {
        func msg(message: String, title: String = "")
        {
            let alertView = UIAlertController(title: title, message: message, preferredStyle: .alert)
            
            alertView.addAction(UIAlertAction(title: "Done", style: .default, handler: {(action: UIAlertAction!) in self.myFunc(alert:alertView)}))
            
            UIApplication.shared.keyWindow?.rootViewController?.present(alertView, animated: true, completion: nil)
        }
        
        func myFunc(alert: UIAlertController) {
            DispatchQueue.main.async(){
                // your code with delay
                alert.dismiss(animated: true, completion: nil)
            }
        }
    }
    class func showError(error:String, viewController:UIViewController) {
        
        var errMessage:String = ""
        errMessage = error
        
        let alert:UIAlertController = Utils.getAlertControllerWithMessage(message: errMessage)
        
        viewController.present(alert, animated: true, completion: nil)
    }
    
    class func getAlertControllerWithMessage(message:String) -> UIAlertController {
        
        let alert = UIAlertController(title: AppConstants.alertTitle, message: message, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
        
        return alert
    }
    
}
