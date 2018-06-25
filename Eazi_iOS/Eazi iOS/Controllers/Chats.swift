//
//  Chats.swift
//  Eazi iOS
//
//  Created by Tejaswini on 30/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class Chats: UIViewController {

    @IBOutlet weak var inviteButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        inviteButton?.layer.cornerRadius = 15
        inviteButton?.clipsToBounds = true
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
