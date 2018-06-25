//
//  EditPhoneNumber.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class EditPhoneNumber: UIViewController {

    @IBOutlet weak var okButton: UIButton!
    @IBOutlet weak var changeButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        changeButton?.layer.cornerRadius = 15
        changeButton?.clipsToBounds = true
        okButton?.layer.cornerRadius = 15
        okButton?.clipsToBounds = true
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func change(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let register_phone_number = mainStoryboard.instantiateViewController(withIdentifier: "RegisterPhoneNumber") as! RegisterPhoneNumber
        self.navigationController?.pushViewController(register_phone_number, animated: true)
    }
    
    @IBAction func submit(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let smsVerification = mainStoryboard.instantiateViewController(withIdentifier: "SmsVerification") as! SmsVerification
        self.navigationController?.pushViewController(smsVerification, animated: true)
    }
    

    
}
