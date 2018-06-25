//
//  LanguageViewController.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class LanguageViewController: UIViewController,UITableViewDelegate,UITableViewDataSource {
    
    var languages=["English","Hindi","German","Spanish","Mandarin","Arabic","Japanese","Korean"]
    
    @IBOutlet weak var chooseLanguage: UIButton!
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return languages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "language_cell", for: indexPath) as! TableViewCell
        cell.language_title.text = languages[indexPath.row]
        return cell
    }
    

    @IBOutlet weak var table_languages: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        table_languages.delegate = self
        table_languages.dataSource = self
        chooseLanguage?.layer.cornerRadius = 15
        chooseLanguage?.clipsToBounds = true
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func submit(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let register_phone_number = mainStoryboard.instantiateViewController(withIdentifier: "RegisterPhoneNumber") as! RegisterPhoneNumber
        self.navigationController?.pushViewController(register_phone_number, animated: true)
    }
    

    

}
