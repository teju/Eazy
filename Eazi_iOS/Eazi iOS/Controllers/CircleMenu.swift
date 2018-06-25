//
//  CircleMenu.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class CircleMenu: UIViewController {

    @IBOutlet weak var searchView: UIView!
    @IBOutlet weak var dialView: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        prepareDefaultCircleMenu()
        searchView?.layer.cornerRadius = 10
        searchView?.clipsToBounds = true
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
   
    
    func prepareDefaultCircleMenu() {
        let screenwidth = UIScreen.main.bounds.width - 300

        // Create circle
        let circle = Circle(with: CGRect(x: screenwidth/2, y: 10, width: 300, height: 300), numberOfSegments: 4, ringWidth: 80.0)
        // Set dataSource and delegate
        circle.dataSource = self
        circle.delegate = self
        
        // Position and customize
       // circle.center = view.center
        
        // Create overlay with circle
        
        // Add to view
        self.dialView.addSubview(circle)
    }
    
   
}


extension CircleMenu: CircleDelegate, CircleDataSource {
    
    func circle(_ circle: Circle, didMoveTo segment: Int, thumb: CircleThumb) {
        if(segment == 1) {
            let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
            let invite = mainStoryboard.instantiateViewController(withIdentifier: "Invite") as! Invite
            self.navigationController?.pushViewController(invite, animated: true)
        }
    }
    
    func circle(_ circle: Circle, iconForThumbAt row: Int) -> UIImage {
        if(row == 1) {
            return UIImage(named: "plus")!
        } else if(row == 2) {
            return UIImage(named: "sarch")!
        }else if(row == 3) {
            return UIImage(named: "settings")!
        } else  {
            return UIImage(named: "connect")!
        }
    }
    
}

