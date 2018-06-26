//
//  AppDelegate.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit
import XMPPFramework
import IQKeyboardManagerSwift
import UserNotifications

protocol ChatDelegate {
    func buddyWentOnline(name: String)
    func buddyWentOffline(name: String)
    func didDisconnect()
}

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate ,UNUserNotificationCenterDelegate ,XMPPRosterDelegate, XMPPStreamDelegate {
    var loaderFlag = false
    var xmppStream = XMPPStream()
    let xmppRosterStorage = XMPPRosterCoreDataStorage()
    var xmppRoster: XMPPRoster
    var window: UIWindow?

    override init() {
        xmppRoster = XMPPRoster(rosterStorage: xmppRosterStorage)
    }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        DDLog.add(DDTTYLogger.sharedInstance)
        IQKeyboardManager.sharedManager().enable = true
        application.registerUserNotificationSettings(UIUserNotificationSettings(types: [.sound, .badge, .alert], categories: nil))
        UNUserNotificationCenter.current().delegate = self
        setupStream()
        // Override point for customization after application launch.
        return true
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,  willPresent notification: UNNotification, withCompletionHandler   completionHandler: @escaping (_ options:   UNNotificationPresentationOptions) -> Void) {
        print(" notifications123 Handle push from foreground ")
        
        completionHandler(.alert)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        
        print(" notifications123 Handle push from background ")
       
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    //MARK: Private Methods
    private func setupStream() {
        //xmppRoster = XMPPRoster(rosterStorage: xmppRosterStorage)
        xmppRoster.activate(xmppStream)
        xmppRoster.addDelegate(self, delegateQueue: DispatchQueue.main)
    }
    
    private func goOnline() {
        let presence = XMPPPresence()
        let domain = xmppStream?.myJID.domain
        
        if domain == "gmail.com" || domain == "gtalk.com" || domain == "talk.google.com" {
            let priority = DDXMLElement.element(withName: "priority", stringValue: "24") as! DDXMLElement
            presence?.addChild(priority)
        }
        xmppStream?.send(presence)
    }
    
    private func goOffline() {
        let presence = XMPPPresence(type: "unavailable")
        xmppStream?.send(presence)
    }
    
    func connect() -> Bool {
        if !(xmppStream?.isConnected())! {
            let user_name = UserDefaults.standard.string(forKey: AppConstants.userDefaults.user_data.rawValue)

            let jabberID = user_name!+"@eazi.ai"
            let myPassword = UserDefaults.standard.string(forKey: AppConstants.userDefaults.user_pass.rawValue)
            
            if !(xmppStream?.isDisconnected())! {
                return true
            }
            if jabberID == nil && myPassword == nil {
                return false
            }
            self.xmppStream = XMPPStream()

            xmppStream?.myJID = XMPPJID(string: jabberID)
            self.xmppStream?.hostName = "198.74.57.124"
            self.xmppStream?.hostPort = 5222
            self.xmppStream?.startTLSPolicy = XMPPStreamStartTLSPolicy.allowed
            self.xmppStream?.enableBackgroundingOnSocket = true;
            xmppStream?.addDelegate(self, delegateQueue: DispatchQueue.main)

            do {
                try xmppStream?.connect(withTimeout: XMPPStreamTimeoutNone)
                
                return true
            } catch {
                print("Something went wrong!")
                return false
            }
        } else {
            return true
        }
    }
    
    func disconnect() {
        goOffline()
        xmppStream?.disconnect()
    
    }
    
    func sendMessage(message : String, to_user : String) {
        print("didSelectRowAt \(to_user)")
        
        let user = XMPPJID(string: to_user+"@eazi.ai")
        let msg = XMPPMessage(type: "chat", to: user)
        msg?.addBody(message)
        self.xmppStream?.send(msg)
        
    }
    
    func xmppStreamDidConnect(_ stream: XMPPStream!) {
        print("Stream: Connected")
        try! xmppStream?.authenticate(withPassword: UserDefaults.standard.string(forKey: AppConstants.userDefaults.user_pass.rawValue))
    }
    
    func xmppStreamDidAuthenticate(_ sender: XMPPStream!) {
        //self.xmppStream.send(XMPPPresence())
        print("Stream: Authenticated")
        self.goOnline()
    }
    
    func xmppStream(sender: XMPPStream!, didReceiveIQ iq: XMPPIQ!) -> Bool {
        print("Did receive IQ")
        return false
    }
    
    func xmppStream(sender: XMPPStream!, didReceiveMessage message: XMPPMessage!) {
        print("Did receive message \(message)")
    }
    
    func xmppStream(sender: XMPPStream!, didSendMessage message: XMPPMessage!) {
        print("Did send message \(message)")
    }
    
    func xmppStream(sender: XMPPStream!, didReceivePresence presence: XMPPPresence!) {
        let presenceType = presence.type()
        let myUsername = sender.myJID.user
        let presenceFromUser = presence.from().user
        
        if presenceFromUser != myUsername {
            print("Did receive presence from \(presenceFromUser)")
            if presenceType == "available" {
               // delegate.buddyWentOnline(name: "\(presenceFromUser)@gmail.com")
            } else if presenceType == "unavailable" {
                //delegate.buddyWentOffline(name: "\(presenceFromUser)@gmail.com")
            }
        }
    }
    func xmppStream(_ sender: XMPPStream!, didReceive message: XMPPMessage!) {
        let from_user = (message.attribute(forName: "from")?.stringValue!)?.components(separatedBy: "/")
        
        let insert = SqliteDataBase.instance.addMessage(cmessage: Utils.htmlToString(mstring: (message.forName("body")?.xmlString)!), cTo_User: (message.attribute(forName: "to")?.stringValue!)!, cFrom_User: from_user![0], cDateTimev: Utils.getCurrentDate(), cisMine: "false")
        print("SqliteDataBase didReceive addMessage \(from_user![0])")
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "load"), object: nil)
        Utils.setupNotificationReminder()
    }
    
    func xmppStream(_ sender: XMPPStream!, didSend message: XMPPMessage!) {
        let user_name = UserDefaults.standard.string(forKey: AppConstants.userDefaults.user_data.rawValue)

        let from_user = (message.attribute(forName: "to")?.stringValue!)?.components(separatedBy: "/")
        let insert = SqliteDataBase.instance.addMessage(cmessage: Utils.htmlToString(mstring: (message.forName("body")?.xmlString)!), cTo_User: user_name!+"@eazi.ai", cFrom_User:from_user![0] , cDateTimev: Utils.getCurrentDate(), cisMine: "true")
        print("SqliteDataBase didSendMessage addMessage \(from_user![0])")
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "load"), object: nil)
        
    }
    func xmppRoster(_ sender: XMPPRoster!, didReceiveRosterItem item: DDXMLElement!) {
        print("Did receive Roster item")
    }
}
