//
//  ChatViewController.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/06/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit
import Contacts

class ChatViewController: UIViewController,UITableViewDelegate,UITableViewDataSource{

    @IBOutlet weak var statusView: UIView!
    @IBOutlet weak var username: UILabel!
    @IBOutlet weak var tableview: UITableView!
    @IBOutlet weak var textfield: UITextField!
    var messages : [Messages]?
    var to_user : String?
    var userN : String?

    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableview.estimatedRowHeight = 80;
        self.tableview.rowHeight = UITableViewAutomaticDimension;
        tableview.dataSource = self
        tableview.delegate = self
        Utils.addLine(button: textfield,color:UIColor.black.cgColor)
        textfield.delegate = self
        NotificationCenter.default.addObserver(self, selector: #selector(loadList), name: NSNotification.Name(rawValue: "load"), object: nil)
        loadList()
    
        statusView.layer.cornerRadius      = statusView.bounds.width / 2
    }
    
    @objc func loadList(){
       
        if(!(to_user?.starts(with: "91"))!) {
            to_user = "91"+to_user!
        }
        to_user = to_user?.replacingOccurrences(of: " ", with: "")
        

        if(!(to_user?.contains("@eazi.ai"))!) {
            to_user = to_user!+"@eazi.ai"
        }
    
        messages = SqliteDataBase.instance.getMessages(user: to_user!)
        if((messages?.count)! > 8 ){
            let lastRow: Int = self.tableview.numberOfRows(inSection: 0) - 1
            let indexPath = IndexPath(row: lastRow, section: 0);
            self.tableview.scrollToRow(at: indexPath, at: .top, animated: false)
        }
        if(userN != nil) {
            username.text = userN
        } else {
            let user_name = Utils.getContactName(to_user: to_user!)
            if(user_name.count > 0) {
                username.text = user_name
            } else {
                username.text = to_user?.replacingOccurrences(of: "@eazi.ai", with: "")
            }
        }
        //load data here
        self.tableview.reloadData()
    }
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(true)
        username.text = ""
        userN = ""
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func send(_ sender: Any) {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        
       appDelegate.sendMessage(message: textfield.text!, to_user: to_user!)
        textfield.text = ""
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return (messages?.count)!
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "chat_cell", for: indexPath) as! TableViewCell
        //cell.msg_bg.backgroundColor = UIColor(patternImage: changeImage().image!)

        cell.messages.text = messages![indexPath.row].message
        let backgroundImage = UIImageView(frame: CGRect(x: 0, y: 0, width: cell.msg_bg.frame.width+10, height: cell.messages.frame.height))
        backgroundImage.image = UIImage(named: "banner1")
        backgroundImage.contentMode = .scaleToFill
        cell.msg_bg.backgroundColor = Utils.hexStringToUIColor(hex: "#85B4F8")
        if(messages![indexPath.row].isMine == "true") {
            cell.leftConstraint.constant = 65
            cell.rightConstraint.constant = 12
        } else{
            cell.rightConstraint.constant = 68
            cell.leftConstraint.constant = 0
        }
         cell.msg_bg.layer.cornerRadius      = 20
            cell.msg_bg.layer.masksToBounds = true
        //cell.msg_bg.insertSubview(backgroundImage, at: 0)
        
        return cell
    }
    
    @IBAction func back(_ sender: Any) {
       dismiss(animated: true, completion: nil)
    }
    
 
    
}

extension ChatViewController:UITextFieldDelegate
{
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
      
        textField.resignFirstResponder()
        
        return true
    }
}
