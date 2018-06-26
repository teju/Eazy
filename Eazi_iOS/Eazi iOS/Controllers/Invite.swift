//
//  Invite.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class Invite: UIViewController,FlowingMenuDelegate {
    @IBOutlet var flowingMenuTransitionManager: FlowingMenuTransitionManager!
    var menu: UIViewController?
    let PresentSegueName = "PresentMenuSegue"
    let DismissSegueName = "DismissMenuSegue"
    let mainColor  = Utils.hexStringToUIColor(hex: "#e0e6ec")
    var pageMenu : CAPSPageMenu?
    static var navController : UINavigationController?
    @IBOutlet weak var inviteButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
    
    }
    
    override func viewDidAppear(_ animated: Bool) {
        print("viewDidAppear")
        flowingMenuTransitionManager.setInteractivePresentationView(view)
        flowingMenuTransitionManager.delegate = self
        inviteButton?.layer.cornerRadius = 15
        inviteButton?.clipsToBounds = true
        pageM()
        Invite.navController = self.navigationController
        let button = UIButton(frame: CGRect(x: UIScreen.main.bounds.width - 90 , y: UIScreen.main.bounds.height - 100, width: 60, height: 60))
        button.backgroundColor = Utils.hexStringToUIColor(hex: "#ff9d86")
        button.addTarget(self, action: #selector(buttonAction), for: .touchUpInside)
        button.setImage(UIImage(named: "plus-1"), for: .normal)
        button.layer.cornerRadius = 0.5 * button.bounds.size.width
        button.clipsToBounds = true
        self.view.addSubview(button)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        print("viewDidAppear viewWillDisappear")
    }
    override func viewDidDisappear(_ animated: Bool) {
        print("viewDidAppear viewWillDisappear")

    }
    override func viewWillAppear(_ animated: Bool) {
        print("viewDidAppear  ")

    }
    @objc func buttonAction(sender: UIButton!) {
//        print("Button tapped")
//        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
//        let profilePage = mainStoryboard.instantiateViewController(withIdentifier: "ProfilePage") as! ProfilePage
//        self.navigationController?.pushViewController(profilePage, animated: true)
        
    }
    @IBAction func profile_page(_ sender: Any) {
      
    }
    
    func flowingMenuNeedsPresentMenu(_ flowingMenu: FlowingMenuTransitionManager) {
        performSegue(withIdentifier: PresentSegueName, sender: self)
    }
    
    func flowingMenuNeedsDismissMenu(_ flowingMenu: FlowingMenuTransitionManager) {
        menu?.performSegue(withIdentifier: DismissSegueName, sender: self)
    }
    
    @IBAction func unwindToMainViewController(_ sender: UIStoryboardSegue) {
        
    }
    
    func colorOfElasticShapeInFlowingMenu(_ flowingMenu: FlowingMenuTransitionManager) -> UIColor? {
        return mainColor
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == PresentSegueName {
            let vc                   = segue.destination
            vc.transitioningDelegate = flowingMenuTransitionManager
            
            flowingMenuTransitionManager.setInteractiveDismissView(vc.view)
            
            menu = vc
        }
    }

    func pageM (){
        var controllerArray : [UIViewController] = []
        
        let chats = Chats(nibName: "Chats", bundle: nil)
        chats.title = "Chats"
        controllerArray.append(chats)
        
        let contacts = Contacts(nibName: "Contacts", bundle: nil)
        contacts.title = "Contacts"
        controllerArray.append(contacts)
        
        let favourite = Favourite(nibName: "Favourite", bundle: nil)
        favourite.title = "Favourite"
        controllerArray.append(favourite)
        
        let groups = Groups(nibName: "Groups", bundle: nil)
        groups.title = "Groups"
        controllerArray.append(groups)
        
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
            .centerMenuItems(true)
        ]
        
        // Initialize scroll menu
        pageMenu = CAPSPageMenu(viewControllers: controllerArray, frame: CGRect(x: 0.0, y: 100.0, width: self.view.frame.width, height: self.view.frame.height), pageMenuOptions: parameters)
        
        self.addChildViewController(pageMenu!)
        self.view.addSubview(pageMenu!.view)
    }
    
    @IBAction func back(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let circleMenu = mainStoryboard.instantiateViewController(withIdentifier: "CircleMenu") as! CircleMenu
        self.navigationController?.pushViewController(circleMenu, animated: true)
    }
}
