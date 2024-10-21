package main

import (
	"net/http"
	"path/filepath"
)

func main() {
	http.HandleFunc("/showImage", func(w http.ResponseWriter, r *http.Request) {
		filePath := r.URL.Query().Get("path") // 从请求中获取文件路径参数
		// 确保filePath是安全的，避免路径遍历等安全问题
		safePath := filepath.Join("C:\\Users\\错误\\Desktop\\demo", filepath.Base(filePath))
		
		http.ServeFile(w, r, safePath) // 直接在HTTP响应中提供文件
	})

	http.ListenAndServe(":8090", nil) // 启动服务器
}