//
//  RegisterPhoneNumber.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class RegisterPhoneNumber: UIViewController {

    @IBOutlet weak var country: UITextField!
    @IBOutlet weak var phone_number: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        Utils.addLine(button: country,color:UIColor.white.cgColor)
        Utils.addLine(button: phone_number,color:UIColor.white.cgColor)
        refresh()
    }
    
    let colors = Colors()
    
    func refresh() {
        view.backgroundColor = UIColor.clear
        var backgroundLayer = colors.gl
        backgroundLayer?.frame = view.frame
        view.layer.insertSublayer(backgroundLayer!, at: 0)
    }



    @IBAction func submit(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let editPhoneNumber = mainStoryboard.instantiateViewController(withIdentifier: "EditPhoneNumber") as! EditPhoneNumber
        self.navigationController?.pushViewController(editPhoneNumber, animated: true)
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
