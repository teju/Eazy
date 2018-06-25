//
//  SmsVerification.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class SmsVerification: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        resendSmsButton?.layer.cornerRadius = 15
        resendSmsButton?.clipsToBounds = true
        okButton?.layer.cornerRadius = 15
        okButton?.clipsToBounds = true
        // Do any additional setup after loading the view.
    }
    
    @IBOutlet weak var resendSmsButton: UIButton!
    @IBOutlet weak var okButton: UIButton!
    
    @IBAction func submit(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let circleMenu = mainStoryboard.instantiateViewController(withIdentifier: "CircleMenu") as! CircleMenu
        self.navigationController?.pushViewController(circleMenu, animated: true)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func resend(_ sender: Any) {
        
    }
}
