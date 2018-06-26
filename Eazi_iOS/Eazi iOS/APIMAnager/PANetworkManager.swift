//
//  PANetworkManager.swift
//  ProductivityApp
//
//  Created by Saltmines-Mac6 on 4/15/17.
//  Copyright © 2017 sandy@LOgicbox. All rights reserved.
//

import UIKit
import SwiftyJSON
class PANetworkManager: NSObject
{
    
    static let sharedManager =  PANetworkManager()
    let error = NSError(domain: "", code: 0, userInfo: [NSLocalizedDescriptionKey : "Object does not exist"])
  

    override private init() {
        
    } //This prevents others from using the default
    
    func performOperationWithURL(url:String, param:String, httpMethod:String,dataDict:Data, completionHandler:@escaping ((_ response:[String : Any]?, _ error: NSError?) -> Void))
    {

        if !Reachability.isConnectedToNetwork(){
            let Alert = Utils.alert()
            Alert.msg(message: AppConstants.AlertMessage.noInternet.rawValue)
            print("Internet Connection Not Available!")
            DispatchQueue.main.async(execute: { () -> Void in
                completionHandler(nil , nil )
            })
            return
        }
        
        let urlStr = NetworkConstants.rootURL+url
        let url =  NSURL(string:  urlStr)
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url! as URL)
        request.httpMethod = httpMethod
        if(httpMethod != nil) {
            request.httpBody = dataDict
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        }
        let string1 = String(data: dataDict, encoding: String.Encoding.utf8) ?? "Data could not be printed"

        print("URLIS  \(url as Any)")
        print("URLIS PARAMS \(string1) \(httpMethod)")

        //set other header fields
        // self.setHeaderFieldsForRequest(request: request)
        
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) -> Void in
            do
            {
                print("URLIS PARAMS \(response)")

                if(response == nil ) {
                    DispatchQueue.main.async(execute: { () -> Void in
                        completionHandler(nil ,error! as NSError )
                    })
                }
                
                if let dataObject = data
                {
                    let json = try JSONSerialization.jsonObject(with: dataObject)

                    do {
                        if let jsonArray = json as? [String:Any]{
                            print("json is array", jsonArray)
                            DispatchQueue.main.async(execute: { () -> Void in
                                completionHandler(jsonArray  ,nil)
                            })
                        } else if let jsonDictionary = json as? [String:Any] {
                            DispatchQueue.main.async(execute: { () -> Void in
                                completionHandler(jsonDictionary  ,nil)
                            })
                            print("json is dictionary", jsonDictionary)
                        } else {
//                            DispatchQueue.main.async(execute: { () -> Void in
//                                completionHandler(json as! [String : Any]  ,nil)
//                            })
                            let string1 = String(data: data!, encoding: String.Encoding.utf8) ?? "Data could not be printed"

                            print("This should never be displayed \(string1)")
                        }
                    }
                   
                }
            }
            catch
            {
                DispatchQueue.main.async(execute: { () -> Void in
                    completionHandler(nil ,error as NSError)
                })
            }
            
        }
        task.resume()
    }
    
    func performOperationWithURL(url:String, param:String , httpMethod:String,dataDict:String, completionHandler:@escaping ((_ response:String?, _ error: NSError?) -> Void))
    {
        if !Reachability.isConnectedToNetwork(){
            let Alert = Utils.alert()
            Alert.msg(message: AppConstants.AlertMessage.noInternet.rawValue)
            print("Internet Connection Not Available!")
            DispatchQueue.main.async(execute: { () -> Void in
                completionHandler(nil , nil )
            })
            return
        }
       
        print("URLIS PARAMS \(String(describing: dataDict.data(using: String.Encoding.utf8)))")
        
        let session = URLSession.shared
        let urlStr = NetworkConstants.rootURL+url
        let url =  NSURL(string:  urlStr)
        let request = NSMutableURLRequest(url: url as! URL )
        request.httpMethod = httpMethod
        print("URLIS  \(url as Any)")

        
        let task = session.downloadTask(with: request as URLRequest) {
            (
            location, response, error) in
            print("errooor \(String(describing: response))")
            if(response == nil ) {
                print("error \(String(describing: error))")
                DispatchQueue.main.async(execute: { () -> Void in
                    completionHandler("error as? String" ,error as NSError?)
                })
                return
            }
            
            guard let _:NSURL = location! as NSURL, let _:URLResponse = response, error == nil else {
                print("error")
                DispatchQueue.main.async(execute: { () -> Void in
                    completionHandler("error as? String" ,error as NSError?)
                })
                return
            }
            
            let urlContents = try! NSString(contentsOf: location!, encoding: String.Encoding.utf8.rawValue)
            
            guard let _:NSString = urlContents else {
                print("error")
                DispatchQueue.main.async(execute: { () -> Void in
                    completionHandler("error as? String" ,error as NSError?)
                })
                return
            }
            DispatchQueue.main.async(execute: { () -> Void in
                completionHandler(urlContents as String ,nil)
            })
            print("urlContents \(urlContents)")
            
        }
        task.resume()
    }
    
    func hitGetRequest(id:String)
    {
        var request = URLRequest(url: URL(string: "http://139.59.8.78/happystreet/index.php/offers/insertDeviceToken?deviceId=\(id)&os_type=1")!)
        request.httpMethod = "GET"
        let session = URLSession.shared
        session.dataTask(with: request) {data, response, err in
            print("Entered the completionHandler")
            }.resume()
    }
}
