//
//  SqliteDataBase.swift
//  ProductivityApp
//
//  Created by IdeaMac on 08/12/17.
//  Copyright © 2017 sandy@LOgicbox. All rights reserved.
//

import Foundation
import SQLite


class SqliteDataBase {
    static let instance:SqliteDataBase = SqliteDataBase()
    private let db: Connection?
    private let message = Table("Messages")
    private let users = Table("Users")

    //Location
    private let id = Expression<Int64>("id")
    private let messages = Expression<String?>("messages")
    private let From_User = Expression<String?>("From_User")
    private let To_User = Expression<String>("To_User")
    private let DateTimev = Expression<String>("DateTimev")
    private let isMine = Expression<String>("isMine")
    var requestUTCDate = ""

    private let userid = Expression<Int64>("userid")
    private let userPhNo = Expression<String?>("userPhNo")
    private let me = Expression<String>("me")

    private init() {
        let path = NSSearchPathForDirectoriesInDomains(
            .documentDirectory, .userDomainMask, true
            ).first!
        print (" SqliteDataBase path \(path)")
        do {
            db = try Connection("\(path)/eazy.sqlite3")
        } catch {
            db = nil
            print (" SqliteDataBase Unable to open database")
        }
        
        createTable()
    }
    

    func createTable() {
        do {
            print("SqliteDataBase createTable")
            try db!.run(message.create(ifNotExists: true) { table in
                table.column(id, primaryKey: true)
                table.column(messages)
                table.column(To_User)
                table.column(From_User)
                table.column(DateTimev)
                table.column(isMine)
            })
            try db!.run(users.create(ifNotExists: true) { table in
                table.column(userid, primaryKey: true)
                table.column(userPhNo,unique: true)
                table.column(me)
    
            })
        } catch {
            print("SqliteDataBase Unable to create table\(error)")
        }
    }
    
    func addMessage(cmessage: String, cTo_User: String, cFrom_User: String,cDateTimev:String,cisMine:String) -> Int64? {
        print("cisMine \(cisMine) ")

        do {
            let insert = message.insert(messages <- cmessage, To_User <- cTo_User, From_User <- cFrom_User,DateTimev  <- cDateTimev,isMine <- cisMine)
            let id = try db!.run(insert)
            print("SqliteDataBase addLocation \(id)")
            return id
        } catch {
            print(" SqliteDataBase addLocation Insert failed \(error)")
            return -1
        }
    }
    
    func addusers(cuserPh: String) -> Int64? {
        print("SqliteDataBase addusers \(cuserPh) ")
        let user_name = UserDefaults.standard.string(forKey: AppConstants.userDefaults.user_data.rawValue)
        do {
            let insert = users.insert(userPhNo <- cuserPh, me <- user_name!+"@eazi.ai")
            let id = try db!.run(insert)
            print("SqliteDataBase addusers \(id)")
            return id
        } catch {
            print(" SqliteDataBase addusers Insert failed \(error)")
            return -1
        }
    }
    
    func getMessages(user : String) -> [Messages] {
       var mesages = [Messages]()
        var user_name = UserDefaults.standard.string(forKey: AppConstants.userDefaults.user_data.rawValue)
        if(!(user_name?.contains("@eazi.ai"))!) {
            user_name = user_name!+"@eazi.ai"
        }
        let to_user = " And To_User = '"+user_name!+"'"
        let whereClause = " From_User = '"+user+"' "+to_user
        let query = " Select * from Messages where "+whereClause
        print("query \(query)")
        do {
            for msg in try db!.prepare(query)   {
                print("getMessages \(msg[5]) \(user) ")

                mesages.append(Messages(
                    message: msg[1]! as! String,
                    to_user: msg[2] as! String,
                    from_user: msg[3]! as! String,
                    isMine: msg[5] as! String))
            }
        } catch {
            print("SqliteDataBase Select failed")
        }
        return mesages
    }
    func getUsers() -> [Users] {
        var users = [Users]()
        var user_name = UserDefaults.standard.string(forKey: AppConstants.userDefaults.user_data.rawValue)
        if(!(user_name?.contains("@eazi.ai"))!) {
            user_name = user_name!+"@eazi.ai"
        }
        let whereClause = " me = '"+user_name!+"'"
        let query = " Select * from Users where "+whereClause
        print("SqliteDataBase query \(query)")
        do {
            for msg in try db!.prepare(query)   {
                print("getMessages \(msg[1]) \(user_name) ")
                users.append(Users(userPh: msg[1] as! String, me: user_name!))
            }
        
        } catch {
            print("SqliteDataBase Select failed")
        }
        return users
    }
//    func getMessages(user : String) -> [Messages] {
//        var mesages = [Messages]()
//        let query = "Select * from Messages where To_User = '"+user+"'"
//        print("getMessages \(query)")
//
//        do {
//            for msg in try db!.prepare(query)   {
//                print("getMessages \(msg[0]) \(msg[1])")
//
//                //                mesages.append(Messages(
//                //                    message: msg[id] as! String,
//                //                    to_user: msg[To_User]as! String,
//                //                    from_user: msg[From_User]!as! String,
//                //                    isMine: msg[isMine] as! String))
//            }
//        } catch {
//            print("SqliteDataBase Select failed")
//        }
//        return mesages
//    }
    
    
    func deleteContact()  {
        do {
           // try db!.run(self.messages.delete())
        } catch {
            print("Delete deleteContact failed")
        }
    }
}
