//
//  ProfilePage.swift
//  Eazi iOS
//
//  Created by Tejaswini on 14/06/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit
class ProfilePage: UIViewController {

    @IBOutlet weak var bg_image: UIImageView!
    @IBOutlet weak var tabs_view: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        pageM()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func pageM (){
        var controllerArray : [UIViewController] = []
        
        let status = Contacts(nibName: "Contacts", bundle: nil)
        status.title = "Status"
        controllerArray.append(status)
        
        let contacts = Contacts(nibName: "Contacts", bundle: nil)
        contacts.title = "Message"
        controllerArray.append(contacts)
        
       
        // Customize menu (Optional)
        let parameters: [CAPSPageMenuOption] = [
            .scrollMenuBackgroundColor(Utils.hexStringToUIColor(hex: "#ffffff")),
            .viewBackgroundColor(Utils.hexStringToUIColor(hex: "#ffffff")),
            .selectionIndicatorColor(Utils.hexStringToUIColor(hex: "#85aff8")),
            .bottomMenuHairlineColor(Utils.hexStringToUIColor(hex: "#ffffff")),
            .menuItemFont(UIFont(name: "HelveticaNeue", size: 13.0)!),
            .menuHeight(50.0),
            .selectedMenuItemLabelColor(Utils.hexStringToUIColor(hex: "#798598")),
            .menuItemWidth(90.0),
            .useMenuLikeSegmentedControl(false),
            .centerMenuItems(false)
        ]
        
        // Initialize scroll menu
        let pageMenu = CAPSPageMenu(viewControllers: controllerArray, frame: CGRect(x: 0.0, y: 0, width: self.view.frame.width, height: self.view.frame.height), pageMenuOptions: parameters)
    
        addChildViewController(pageMenu)
        tabs_view.addSubview(pageMenu.view)
        
        
    }
    
    @IBAction func back(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let invite = mainStoryboard.instantiateViewController(withIdentifier: "Invite") as! Invite
        self.navigationController?.pushViewController(invite, animated: true)
    }
}
