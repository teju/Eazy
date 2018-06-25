//
//  XMPPController.swift
//  Eazi iOS
//
//  Created by Tejaswini on 25/06/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import Foundation
import XMPPFramework

class XMPPController: NSObject, XMPPRosterDelegate {
    var xmppStream: XMPPStream?
    let xmppRosterStorage = XMPPRosterCoreDataStorage()
    var xmppRoster: XMPPRoster?
    let hostName: String
    let userJID: XMPPJID
    let hostPort: UInt16
    let password: String
    
    init(hostName: String, userJIDString: String, hostPort: UInt16 = 5222, password: String) throws {
        let userJID = XMPPJID(string: userJIDString) 
        xmppRoster = XMPPRoster(rosterStorage: xmppRosterStorage)

        self.hostName = hostName
        self.userJID = userJID!
        self.hostPort = hostPort
        self.password = password
        
        // Stream Configuration
        self.xmppStream = XMPPStream()
        self.xmppStream?.hostName = hostName
        self.xmppStream?.hostPort = hostPort
        self.xmppStream?.startTLSPolicy = XMPPStreamStartTLSPolicy.allowed
        self.xmppStream?.myJID = userJID
        xmppRoster?.activate(xmppStream)

        super.init()
        
        self.xmppStream?.addDelegate(self, delegateQueue: DispatchQueue.main)
        self.xmppRoster?.addDelegate(self, delegateQueue: DispatchQueue.main)

    }
    
    func connect() {
        if !(self.xmppStream?.isDisconnected())! {
            return
        }
        try! self.xmppStream?.connect(withTimeout: XMPPStreamTimeoutNone)
        print("xmppStream1234 connected")
        goOnline()

    }
    
    func sendMessage() {
        let user = XMPPJID(string: "919035438658@eazi.ai")
        let msg = XMPPMessage(type: "chat", to: user)
        msg?.addBody("test message")
        self.xmppStream?.send(msg)
        
    }
    
    func goOnline() {
        let presence = XMPPPresence()
        let domain = xmppStream?.myJID.domain
        
        if domain == "gmail.com" || domain == "eazi.in" || domain == "talk.google.com" {
            let priority = DDXMLElement.element(withName: "priority", stringValue: "24") as! DDXMLElement
            presence?.addChild(priority)
        }
        print("setupStream goOnline \(presence)")
        
        xmppStream?.send(presence)
    }
    
    func goOffline() {
        
        let presence = XMPPPresence(type: "unavailable")
        xmppStream?.send(presence)
    }
    
    func disconnect() {
        goOffline()
        xmppStream?.disconnect()
    }
    
//
//    //MARK: XMPP Delegates
//    func xmppStreamDidConnect(sender: XMPPStream!) {
//        do {
//            let xmpp =   try xmppStream?.authenticate(withPassword: "9199640622371234")
//            print("xmppStreamDidConnect \(xmpp)")
//
//        } catch {
//            print("Could not authenticate")
//        }
//    }
//
//    func xmppStreamDidAuthenticate(_ sender: XMPPStream!) {
//        print("xmppStreamDidAuthenticate \(sender.isConnected())")
//
//        goOnline()
//    }
    
    func xmppStream(sender: XMPPStream!, didReceiveIQ iq: XMPPIQ!) -> Bool {
        print("Did receive IQ")
        return false
    }
    
   
    func xmppRoster(_ sender: XMPPRoster!, didReceiveRosterItem item: DDXMLElement!) {
        print("Did receive Roster item")
    }

}
extension XMPPController: XMPPStreamDelegate {
    
    func xmppStreamDidConnect(_ stream: XMPPStream!) {
        print("Stream: Connected")
        try! stream.authenticate(withPassword: self.password)
    }
    
    func xmppStreamDidAuthenticate(_ sender: XMPPStream!) {
        //self.xmppStream.send(XMPPPresence())
        print("Stream: Authenticated")
        self.goOnline()
    }
    
    func xmppStream(_ sender: XMPPStream!, didSend presence: XMPPPresence!) {
        print("sent")
        
    }
    func xmppStream(_ sender: XMPPStream!, didFailToSend presence: XMPPPresence!, error: Error!) {
        print("didFailToSend")
    }
    
    func xmppStream(sender: XMPPStream!, didReceiveMessage message: XMPPMessage!) {
        print("Did receive message \(message)")
    }
    
    func xmppStream(sender: XMPPStream!, didSendMessage message: XMPPMessage!) {
        print("Did send message \(message)")
    }
   
    func xmppStream(_ sender: XMPPStream!, didReceive message: XMPPMessage!) {
        print("Did receive message \(message)")

    }
    
    func xmppStream(sender: XMPPStream!, didReceivePresence presence: XMPPPresence!) {
        print("Did didReceivePresence")

        let presenceType = presence.type()
        let myUsername = sender.myJID.user
        let presenceFromUser = presence.from().user
        
        if presenceFromUser != myUsername {
            print("Did receive presence from \(presenceFromUser)")
            if presenceType == "available" {
                //delegate.buddyWentOnline(name: "\(presenceFromUser)@gmail.com")
            } else if presenceType == "unavailable" {
                //delegate.buddyWentOffline(name: "\(String(describing: presenceFromUser))@gmail.com")
            }
        }
    }
    
}
