//
//  SmsVerification.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class SmsVerification: UIViewController {
    var phoneNumber : String?
    var enteredOtp: String = ""

    @IBOutlet weak var otpView: VPMOTPView!
    @IBOutlet weak var phone_number: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        phone_number.text = phoneNumber

        resendSmsButton?.layer.cornerRadius = 15
        resendSmsButton?.clipsToBounds = true
        okButton?.layer.cornerRadius = 15
        okButton?.clipsToBounds = true
        // Do any additional setup after loading the view.
        
        otpView.otpFieldsCount = 4
        otpView.otpFieldDefaultBorderColor = UIColor.black
        otpView.otpFieldEnteredBorderColor = UIColor.black
        otpView.otpFieldErrorBorderColor = UIColor.black
        otpView.otpFieldBorderWidth = 2
        
        otpView.delegate = self
        
        // Create the UI
        otpView.initalizeUI()
    }
    
    @IBOutlet weak var resendSmsButton: UIButton!
    @IBOutlet weak var okButton: UIButton!
    
    @IBAction func submit(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let invite = mainStoryboard.instantiateViewController(withIdentifier: "Invite") as! Invite
        self.navigationController?.pushViewController(invite, animated: true)

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func resend(_ sender: Any) {
        
    }
}
extension SmsVerification: VPMOTPViewDelegate {
    func hasEnteredAllOTP(hasEntered: Bool) -> Bool {
        print("Has entered all OTP? \(hasEntered)")
        
        return enteredOtp == "12345"
    }
    
    func shouldBecomeFirstResponderForOTP(otpFieldIndex index: Int) -> Bool {
        return true
    }
    
    func enteredOTP(otpString: String) {
        enteredOtp = otpString
        print("OTPString: \(otpString)")
    }
}

