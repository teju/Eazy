//
//  UserChatCellView.swift
//  Eazi iOS
//
//  Created by Tejaswini on 27/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit

class UserChatCellView: UITableViewCell {
    @IBOutlet weak var displayNameLabel: UILabel!
    
    @IBOutlet weak var statusView: UIView!{
        didSet {
            statusView.layer.masksToBounds = true
        }
    }

    
    @IBOutlet weak var avatarImageView: UIImageView!{
        didSet {
            avatarImageView.layer.masksToBounds = true
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // iOS 10 bug: rdar://27644391
        contentView.layoutSubviews()
        
        avatarImageView.layer.cornerRadius = avatarImageView.bounds.width / 2
        statusView.layer.cornerRadius      = statusView.bounds.width / 2
        // Configure the view for the selected state
    }

}
