//
//  ContentView.swift
//  ios
//
//  Created by Mike on 20.05.2020.
//  Copyright © 2020 Elegant. All rights reserved.
//

import SwiftUI
import shared

let config = Configuration()
let appType: String = config.getAppType().displayName

struct ContentView: View {
    let text = "Hello, " + appType
    var body: some View {
        Text(text)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
