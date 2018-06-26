//
//  Messages.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/06/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import Foundation
class Messages {
    var message: String = ""
    var to_user: String = ""
    var from_user: String = ""
    var isMine : String = ""
    init( message: String, to_user: String,from_user: String,isMine:String) {
        self.message = message
        self.to_user = to_user
        self.isMine = isMine
        self.isMine = isMine
    
    }
}
