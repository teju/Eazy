/**
 The MIT License (MIT)
 Copyright (c) 2016 Shoaib Ahmed / Sufi-Al-Hussaini
 
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
//
//  Circle.swift
//  CircleMenu
//
//  Created by Shoaib Ahmed on 11/25/16.
//  Copyright © 2016 Shoaib Ahmed / Sufi-Al-Hussaini. All rights reserved.
//

import UIKit

class Circle: UIView {
    
    enum ThumbSeparatorStyle {
        case none
        case basic
    }
    
    let kRotationDegrees: CGFloat = 90
    
    public var thumbs: NSMutableArray = []
    public var thumbs2: NSMutableArray = []

    public var circle: UIBezierPath?
    public var path: UIBezierPath?
    public var ringWidth: CGFloat
//    public var isOverlayed: Bool = true
    public var isInertiaEffect: Bool = true
    private(set) var isRotate: Bool = true
    public var numberOfSegments: Int
    public var circleColor: UIColor?
    public var separatorColor: UIColor?
    public var separatorStyle: ThumbSeparatorStyle
    public var recognizer: CircleGestureRecognizer = CircleGestureRecognizer()
    public var overlayView: CircleOverlayView?
    
    public var delegate: CircleDelegate?
    public var dataSource: CircleDataSource?
    var ringWidth2 : CGFloat?
    //Circle radius is equal to rect / 2 , path radius is equal to rect1/2.
    required init(with frame: CGRect, numberOfSegments segments: Int, ringWidth width: CGFloat, isRotating rotate: Bool = true, iconWidth: CGFloat = CircleThumb.kIconViewWidth, iconHeight: CGFloat = 30) {
        self.ringWidth = width - 27
        self.numberOfSegments = segments
        self.separatorStyle = .none
        self.circleColor = UIColor.clear
        self.isRotate = false
        ringWidth2 = width - 18
        super.init(frame: frame)
        
        self.recognizer = CircleGestureRecognizer(target: self, action: #selector(tapped))
        self.addGestureRecognizer(self.recognizer)
        self.isOpaque = false
        
        let rect1 = CGRect(x: 0, y: 0, width: frame.height - (2*ringWidth), height: frame.width - (2*ringWidth))
        let rect2 = CGRect(x: 50, y: 50, width: frame.height - (2*ringWidth2!), height: frame.width - (2*ringWidth2!))

        for i in 0..<numberOfSegments {
            let thumb = CircleThumb(with: rect1.size.height/2, longRadius: frame.size.height/2, numberOfSegments: numberOfSegments, iconWidth: iconWidth, iconHeight: iconHeight)
            
            thumbs.add(thumb)
        }
        for _ in 0..<numberOfSegments {
            let thumb = CircleThumbInner(with: rect2.size.height/2, longRadius: frame.size.height/2, numberOfSegments: numberOfSegments)
            thumbs2.add(thumb)
        }
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func draw(_ rect: CGRect) {
        super.draw(rect)
        
        let ctx = UIGraphicsGetCurrentContext()
        ctx?.saveGState()
        ctx?.setBlendMode(.copy)
        self.circleColor?.setFill()
        circle = UIBezierPath(ovalIn: rect)
    
        circle?.close()
        circle?.fill()
        
        var rect1 = CGRect(x: 0, y: 0, width: rect.height - (2*ringWidth), height: rect.width - (2*ringWidth))
        rect1.origin.x = rect.size.width / 2  - rect1.size.width / 2
        rect1.origin.y = rect.size.height / 2  - rect1.size.height / 2
        
        var rect2 = CGRect(x: 0, y: 0, width: rect.height - (2*ringWidth2!), height: rect.width - (2*ringWidth2!))
        rect2.origin.x = rect.size.width / 2  - rect2.size.width / 2
        rect2.origin.y = rect.size.height / 2  - rect2.size.height / 2
        
        path = UIBezierPath(ovalIn: rect1)
        self.circleColor?.setFill()
        path?.fill()
        ctx?.restoreGState()
        
        //Drawing Thumbs
        let fNumberOfSegments: CGFloat = CGFloat(self.numberOfSegments)
        var perSectionDegrees: CGFloat = 360.0/fNumberOfSegments
        let totalRotation: CGFloat = 360.0/fNumberOfSegments
        let centerPoint = CGPoint(x: rect.size.width/2, y: rect.size.height/2)
        let centerPoint2 = CGPoint(x: rect.size.width/2 , y: rect.size.height/2 )

        let textView: UITextView = UITextView(frame: CGRect(x: 50, y: 120, width: self.frame.width - 100, height: 50.00));
        textView.textAlignment = .center
        textView.backgroundColor = UIColor.clear
        textView.font = textView.font?.withSize(20)
        textView.textColor = UIColor.orange
        textView.text = "Settings"
        textView.transform = CGAffineTransform(rotationAngle: CGFloat.pi )

        var deltaAngle: CGFloat?
        for i in 0..<numberOfSegments {
            let thumb = self.thumbs2.object(at: i) as! CircleThumbInner
            thumb.tag = i
            
            let radius: CGFloat = rect2.size.height/2 + ((rect.size.height/2 - rect2.size.height/2)/2) //- thumb.yydifference
            let x: CGFloat = centerPoint2.x + (radius * cos(CircleThumbInner.radiansFrom(degrees: perSectionDegrees)))
            let yi: CGFloat = centerPoint2.y + (radius * sin(CircleThumbInner.radiansFrom(degrees: perSectionDegrees)))
            
            // Rotate thumb itself to align properly along the circumference
            thumb.transform = CGAffineTransform(rotationAngle: CircleThumbInner.radiansFrom(degrees: perSectionDegrees + kRotationDegrees))
            
            if i == 0 {
                deltaAngle = CircleThumbInner.radiansFrom(degrees: 360 - kRotationDegrees) + atan2(thumb.transform.a, thumb.transform.b)
                self.recognizer.currentThumb2 = thumb
                thumb.isSelected = true

            }
            
            //set position of the thumb
            thumb.layer.position = CGPoint(x: x, y: yi)
            perSectionDegrees += totalRotation
            
            self.addSubview(thumb)
            self.addSubview(textView)
        }
        
        for i in 0..<numberOfSegments {
            let thumb = self.thumbs.object(at: i) as! CircleThumb
            thumb.tag = i
            thumb.iconView!.image = self.dataSource?.circle(self, iconForThumbAt: thumb.tag)
            let radius: CGFloat = rect1.size.height/2 + ((rect.size.height/2 - rect1.size.height/2)/2) //- thumb.yydifference
            let x: CGFloat = centerPoint.x + (radius * cos(CircleThumb.radiansFrom(degrees: perSectionDegrees)))
            let yi: CGFloat = centerPoint.y + (radius * sin(CircleThumb.radiansFrom(degrees: perSectionDegrees)))
            
            // Rotate thumb itself to align properly along the circumference
            thumb.transform = CGAffineTransform(rotationAngle: CircleThumb.radiansFrom(degrees: perSectionDegrees + kRotationDegrees))
            
            if i == 0 {
                deltaAngle = CircleThumb.radiansFrom(degrees: 360 - kRotationDegrees) + atan2(thumb.transform.a, thumb.transform.b)
                thumb.iconView.isSelected = true
                self.recognizer.currentThumb = thumb
            }
            
            //set position of the thumb
            thumb.layer.position = CGPoint(x: x, y: yi)
            perSectionDegrees += totalRotation
            
            self.addSubview(thumb)
        }
       
        // Rotate circle slightly to align top thumb perfectly
        self.transform = CGAffineTransform(rotationAngle: deltaAngle!)
    }
    
    @objc func tapped(recognizer: CircleGestureRecognizer) {
        // Rotate circle while rotate gesture is detected
        if !recognizer.isEnded! && isRotate {
            let point = recognizer.location(in: self)
            if !path!.contains(point) {
                self.transform = self.transform.rotated(by: recognizer.rotation!)
            }
        }
    }
}



protocol CircleDelegate {
    
    func circle(_ circle: Circle, didMoveTo segment: Int, thumb: CircleThumb)
    
}


protocol CircleDataSource {
    
    func circle(_ circle: Circle, iconForThumbAt row: Int) -> UIImage

}



