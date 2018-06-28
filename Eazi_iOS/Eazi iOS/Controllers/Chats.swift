//
//  Chats.swift
//  Eazi iOS
//
//  Created by Tejaswini on 30/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class Chats: UIViewController,UITableViewDataSource,UITableViewDelegate {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var inviteButton: UIButton!
    var users : [Users]?

    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.register(UINib(nibName: "CustomChatCell", bundle: nil), forCellReuseIdentifier: "CustomChatCell")
        self.tableView.estimatedRowHeight = 80;
        self.tableView.rowHeight = UITableViewAutomaticDimension;
        tableView.delegate = self
        tableView.dataSource = self
        
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        users = SqliteDataBase.instance.getUsers()
        //print("viewDidAppear ")
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return (users?.count)!
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let placeCell : CustomChatCell = tableView.dequeueReusableCell(withIdentifier: "CustomChatCell") as! CustomChatCell
        let user_name = Utils.getContactName(to_user: users![indexPath.row].userPh)
        if(user_name.count > 0) {
            placeCell.name.text = user_name
        } else {
            placeCell.name.text = users![indexPath.row].userPh.replacingOccurrences(of: "@eazi.ai", with: "")
        }
        return placeCell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let profilePage = mainStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
        profilePage.to_user = users![indexPath.row].userPh
        self.present(profilePage, animated: true, completion: nil)
    }
}
