//
//  ViewController.swift
//  Eazi iOS
//
//  Created by Tejaswini on 26/05/18.
//  Copyright © 2018 Tejaswini. All rights reserved.
//

import UIKit
import XMPPFramework

class ViewController: UIViewController,FSPagerViewDataSource,FSPagerViewDelegate  {

    @IBOutlet weak var banner_desc: UILabel!
    @IBOutlet weak var banner_title: UILabel!
    var xmppController: XMPPController?

    @IBOutlet weak var pagerView: FSPagerView!
        {
        didSet {
            self.pagerView.register(FSPagerViewCell.self, forCellWithReuseIdentifier: "cell")
            self.pagerView.itemSize = .zero
            
        }
    }
    
    @IBOutlet weak var pagercontrol: FSPageControl!
        {
        didSet {
            self.pagercontrol.numberOfPages = self.imageNames.count
            self.pagercontrol.contentHorizontalAlignment = .center
            self.pagercontrol.contentInsets = UIEdgeInsets(top: 10, left: 150, bottom: 10, right: 150)
            self.pagercontrol.interitemSpacing = 30
            self.pagercontrol.setImage(UIImage(named: "pagination_unselect"), for: .normal)
            self.pagercontrol.setImage(UIImage(named: "pagination_select"), for: .selected)
        }
    }
    @IBOutlet weak var agree_button: UIButton!
    var uiview:UIView!
    var banner_titles=["","Chat","Search nearby"]
    var banner_descs=["Your World take control" ,
                       "Express Yourself",
                       "What's around you and beyond"]
    fileprivate let imageNames = ["banner1","banner2","banner3"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.pagerView.automaticSlidingInterval = 3.0 - self.pagerView.automaticSlidingInterval
        self.pagerView.isInfinite = !self.pagerView.isInfinite
        agree_button?.layer.cornerRadius = 15
        agree_button?.clipsToBounds = true
       // setupStream()
        // Do any additional setup after loading the view, typically from a nib.
        
    }
    
    private func setupStream() {
        do {
            try self.xmppController = XMPPController(hostName: "198.74.57.124",
                                                     userJIDString: "919964062237@eazi.ai",
                                                     password: "9199640622371234")
            self.xmppController?.xmppStream?.addDelegate(self, delegateQueue: DispatchQueue.main)
            self.xmppController?.connect()
        } catch {
        }
    }
    @IBAction func agree(_ sender: Any) {
        let mainStoryboard: UIStoryboard = UIStoryboard(name: AppConstants.Storyboards.Main.rawValue, bundle: nil)
        let language = mainStoryboard.instantiateViewController(withIdentifier: "LanguageViewController") as! LanguageViewController
        self.navigationController?.pushViewController(language, animated: true)
        //self.xmppController?.sendMessage()

    }
    
    public func numberOfItems(in pagerView: FSPagerView) -> Int {
        
        return self.imageNames.count
    }
    // MARK:- FSPagerView Delegate
    
    func pagerView(_ pagerView: FSPagerView, didSelectItemAt index: Int) {
        pagerView.deselectItem(at: index, animated: true)
        pagerView.scrollToItem(at: index, animated: true)
        self.pagercontrol.currentPage = index
    }
    func pagerViewDidScroll(_ pagerView: FSPagerView) {
        guard self.pagercontrol.currentPage != pagerView.currentIndex else {
            return
        }
        self.pagercontrol.currentPage = pagerView.currentIndex // Or Use KVO with property "currentIndex"
    }
    
    public func pagerView(_ pagerView: FSPagerView, cellForItemAt index: Int) -> FSPagerViewCell {
        let cell = pagerView.dequeueReusableCell(withReuseIdentifier: "cell", at: index)
        
        cell.imageView?.image = UIImage(named: self.imageNames[index])
        //  cell.imageView?.contentMode = .scaleToFill
        cell.imageView?.clipsToBounds = true
        
        let animation = CATransition()
        animation.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseIn)
        animation.type = kCATransitionPush
        animation.subtype = kCATransitionFromRight
        animation.duration = CFTimeInterval(3.0 - self.pagerView.automaticSlidingInterval)
        banner_desc.layer.add(animation, forKey: nil)
        banner_desc.text = banner_descs[index]
        banner_title.text = banner_titles[index]
        return cell
    }
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

