//
//  MenuViewController.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit
import Contacts

final class MenuViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    @IBOutlet weak var topBar: UINavigationBar!
    @IBOutlet weak var userTableView: UITableView!
    @IBOutlet weak var backButtonItem: UIBarButtonItem!
    
    let CellName  = "UserChatCell"
    
    let mainColor = Utils.hexStringToUIColor(hex: "#e0e6ec")
    var objects  = [CNContact]()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        topBar.tintColor              = .clear
        topBar.barTintColor           = mainColor
        topBar.titleTextAttributes    = [
            NSAttributedStringKey.font: UIFont(name: "HelveticaNeue-Light", size: 22)!,
            NSAttributedStringKey.foregroundColor: UIColor.black
        ]
        userTableView.backgroundColor = mainColor
        view.backgroundColor          = mainColor
        let store = Utils.getContacts()
        userTableView.delegate = self
        userTableView.dataSource = self
        objects = Utils.retrieveContactsWithStore(store: store)
    }
    
    // MARK: - Managing the Status Bar
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    // MARK: - UITableView DataSource Methods
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return objects.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: CellName) as! UserChatCellView
        let contact = self.objects[indexPath.row]
        let fullName = CNContactFormatter.string(from: contact, style: .fullName) ?? "No Name"
        cell.displayNameLabel.text = fullName

        if let imageData = contact.imageData {
            //If so create the image
            let userImage = UIImage(data: imageData)
            cell.avatarImageView.image = userImage;
        }
        
        cell.contentView.backgroundColor = mainColor
        
        return cell
    }
    
    // MARK: - UITableView Delegate Methods
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let contact = self.objects[indexPath.row]
        let actualNumber = contact.phoneNumbers.first?.value as? CNPhoneNumber
        let fullName = CNContactFormatter.string(from: contact, style: .fullName) ?? "No Name"

        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        
        let profilePage = mainStoryboard.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
        profilePage.to_user = actualNumber?.stringValue
        profilePage.userN = fullName
        self.present(profilePage, animated: true, completion: nil)

    }
}
