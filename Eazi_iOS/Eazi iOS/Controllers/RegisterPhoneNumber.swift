//
//  RegisterPhoneNumber.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit
import SwiftyJSON
import DropDown


class RegisterPhoneNumber: UIViewController {

    @IBOutlet weak var selectCountryCode: UIButton!
    @IBOutlet weak var selectCountry: UIButton!
    @IBOutlet weak var phone_number: UITextField!
    var registerequestDict = [String: String]()
    var confirmequestDict = [String: Any]()
    var country_name = [String]()
    var country_code = [String]()
    let chooseDropDown = DropDown()
    //var overview : Overview?
    lazy var dropDowns: [DropDown] = {
        return [
            self.chooseDropDown
        ]
    }()
    override func viewDidLoad() {
        super.viewDidLoad()
        Utils.addLine(button: phone_number,color:UIColor.white.cgColor)
        Utils.addLine(button: selectCountryCode,color:UIColor.white.cgColor)
        Utils.addLine(button: selectCountry,color:UIColor.white.cgColor)
        Utils.placeHolderText(textFiled: phone_number, color: UIColor.lightText, text: "Enter Phone Number")
        getCountries()
        refresh()
        setupChooseDropDown()
       
        dropDowns.forEach { $0.dismissMode = .onTap }
        dropDowns.forEach { $0.direction = .any }
    }
    
    @IBAction func chooseCountry(_ sender: Any) {
        self.chooseDropDown.dataSource = self.country_name
        self.chooseDropDown.selectionAction = { [weak self] (index, item) in
            //self?.overview?.view.alpha = CGFloat(1.0)
            self?.selectCountry?.setTitle(item, for: .normal)
            self?.selectCountryCode?.setTitle(self?.country_code[index], for: .normal)

        }
        self.chooseDropDown.show()

    }
    
    @IBAction func chooseCountryCode(_ sender: Any) {
        self.chooseDropDown.dataSource = self.country_code
        self.chooseDropDown.selectionAction = { [weak self] (index, item) in
            //self?.overview?.view.alpha = CGFloat(1.0)
            self?.selectCountryCode?.setTitle(item, for: .normal)
            self?.selectCountry?.setTitle(self?.country_name[index], for: .normal)

        }
        self.chooseDropDown.show()
    }
    
    func setupChooseDropDown() {
        chooseDropDown.anchorView = self.view
        
        chooseDropDown.bottomOffset = CGPoint(x: 20, y: (selectCountry?.bounds.height)!)
        
        
    }
    let colors = Colors()
    
    func refresh() {
        view.backgroundColor = UIColor.clear
        var backgroundLayer = colors.gl
        backgroundLayer?.frame = view.frame
        view.layer.insertSublayer(backgroundLayer!, at: 0)
    }
    
    func getCountries() {
        
       
        
        Utils.showProgressHudToView(view: self.view, message: "Loading...")
        PANetworkManager.sharedManager.performOperationWithURL(url: WebserviceMethods.country.rawValue,param:"&",
                                                               httpMethod: ApiMethod.get,dataDict:"paramString")
        { (data, error) in
            
            Utils.isProgressHudToView(view: self.view)
            if(error != nil) {
                Utils.showError(error: AppConstants.AlertMessage.genericError.rawValue, viewController: self)
                return
            }
            if let dataFromString =  data?.data(using: String.Encoding.utf8, allowLossyConversion: false) {
                let json = try? JSON(data: dataFromString)
                let countries = json!["countries"]
                for index in 0 ... countries.count - 1 {
                    let country = countries[index]
                    self.country_name.append(country["name"].string!)
                    self.country_code.append(country["code"].string!)
                    print("IsAccountExist \(country["name"])")
                }
                self.selectCountry?.setTitle(self.self.country_name[0], for: .normal)
                self.selectCountryCode?.setTitle(self.country_code[0], for: .normal)
            }
        }
    }
    
    func register() {
        var requestParams = Data()
        
        if let jsonData = try? JSONSerialization.data(withJSONObject: registerequestDict, options: [])
        {
            requestParams = jsonData
        }
        
        Utils.showProgressHudToView(view: self.view, message: "Loading...")
        PANetworkManager.sharedManager.performOperationWithURL(url:WebserviceMethods.register.rawValue,param :"&",httpMethod: ApiMethod.put,dataDict:requestParams)
        { (data, error) in
            
            Utils.isProgressHudToView(view: self.view)
            if(error != nil) {
                Utils.showError(error: AppConstants.AlertMessage.genericError.rawValue, viewController: self)
                return
            }

            self.confirm()
        }
    }
    
    func confirm() {
        var requestParams = Data()
        
        if let jsonData = try? JSONSerialization.data(withJSONObject: confirmequestDict, options: [])
        {
            requestParams = jsonData
        }
        
        Utils.showProgressHudToView(view: self.view, message: "Loading...")
        PANetworkManager.sharedManager.performOperationWithURL(url:WebserviceMethods.confirm.rawValue,param :"&",httpMethod: ApiMethod.put,dataDict:requestParams)
        { (data, error) in
            
            Utils.isProgressHudToView(view: self.view)
            if(error != nil) {
                Utils.showError(error: AppConstants.AlertMessage.genericError.rawValue, viewController: self)
                return
            }
            let username = data!["username"] as! String
            let userpass = data!["password"] as! String

            print("userNAme \(UserDefaults.standard.bool(forKey: AppConstants.userDefaults.isLoggedIn.rawValue))")
            UserDefaults.standard.setValue(username, forKey: AppConstants.userDefaults.user_data.rawValue)
            UserDefaults.standard.setValue(userpass, forKey: AppConstants.userDefaults.user_pass.rawValue)
            if(UserDefaults.standard.bool(forKey: AppConstants.userDefaults.isLoggedIn.rawValue)) {
                let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
                let circleMenu = mainStoryboard.instantiateViewController(withIdentifier: "CircleMenu") as! CircleMenu
                self.navigationController?.pushViewController(circleMenu, animated: true)
            } else {
                let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
                let editPhoneNumber = mainStoryboard.instantiateViewController(withIdentifier: "EditPhoneNumber") as! EditPhoneNumber
                editPhoneNumber.phoneNumber = self.phone_number.text
                self.navigationController?.pushViewController(editPhoneNumber, animated: true)
            }
            
            let appDelegate = UIApplication.shared.delegate as! AppDelegate
            
            if appDelegate.connect() {
                
            }
        }
    }

    @IBAction func submit(_ sender: Any) {
        if(validate()) {
            registerequestDict["mdn"] = "91"+phone_number.text!
            registerequestDict["type"] = "ios"
            registerequestDict["device_id"] = "Abcdefg"
            confirmequestDict["mdn"] = "91"+phone_number.text!
            confirmequestDict["code"] = 1234
            register()
        }
    }
    
    func validate() -> Bool {
        if(Validations.isStringEmpty(string: phone_number.text!)) {
            Utils.showError(error:AppConstants.AlertMessage.enterPNumb.rawValue, viewController: self)
            return false
        }
        return true
    }
}

class Colors {
    var gl:CAGradientLayer!
    
    init() {
        let colorTop = UIColor(red: 133.0 / 255.0, green: 180.0 / 255.0, blue: 248.0 / 255.0, alpha: 1.0).cgColor
        let colorBottom = UIColor(red: 255.0 / 255.0, green: 150.0 / 255.0, blue: 224.0 / 255.0, alpha: 1.0).cgColor
        
        self.gl = CAGradientLayer()
        self.gl.colors = [colorTop, colorBottom]
        self.gl.locations = [0.0, 1.0]
    }
}
