import React, { useState, useEffect } from 'react';
import { getAllMessages } from '../api';
import { Comment } from '../types';
import PostMessage from './postMessage';

interface MessageListProps {
    refreshKey: number; // 接收刷新键
}

const MessageList: React.FC<MessageListProps> = ({ refreshKey }) => {
    const [comments, setComments] = useState<Comment[]>([]);
    const [replyingTo, setReplyingTo] = useState<number | null>(null);
    const [loading, setLoading] = useState(true); // 恢复loading状态

    const fetchComments = async () => {
        try {
            setLoading(true); 
            const data = await getAllMessages();
            setComments(data);
        } catch (error) {
            console.error('获取留言失败:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchComments();
    }, [refreshKey]); 

    const handleNewComment = () => {
        fetchComments(); 
    };

    const renderComments = (comments: Comment[], level = 0) => {
        return comments.map((comment) => (
            <div key={comment.id} style={{ 
                marginLeft: `${level * 20}px`,
                marginBottom: '20px',
                borderLeft: level > 0 ? '2px solid #ddd' : 'none',
                paddingLeft: level > 0 ? '10px' : '0'
            }}>
                <div style={{ 
                    backgroundColor: '#f9f9f9',
                    padding: '15px',
                    borderRadius: '4px'
                }}>
                    <p style={{ margin: '0 0 8px 0' }}>{comment.content}</p>
                    <p style={{ 
                        color: '#666', 
                        fontSize: '0.85em',
                        margin: '0 0 10px 0'
                    }}>
                        发布者: {comment.username} | 发表时间: {new Date(comment.createdAt).toLocaleString()}
                    </p>
                    
                    <button
                        onClick={() => setReplyingTo(replyingTo === comment.id ? null : comment.id)}
                        style={{
                            padding: '6px 12px',
                            background: '#f0f0f0',
                            border: '1px solid #ddd',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '0.85em'
                        }}
                    >
                        {replyingTo === comment.id ? '取消回复' : '回复'}
                    </button>
                    
                    {replyingTo === comment.id && (
                        <div style={{ marginTop: '15px' }}>
                            <PostMessage 
                                parentId={comment.id} 
                                onSuccess={() => {
                                    setReplyingTo(null);
                                    handleNewComment();
                                }}
                            />
                        </div>
                    )}
                </div>
                
                {comment.replies && comment.replies.length > 0 && (
                    renderComments(comment.replies, level + 1)
                )}
            </div>
        ));
    };

    return (
        <div>
            <h2 style={{ marginBottom: '20px' }}>留言列表</h2>
            
            {loading ? (
                <div>加载中...</div>
            ) : (
                <>
                    {comments.length > 0 ? (
                        renderComments(comments)
                    ) : (
                        <p style={{ color: '#666' }}>暂无留言，快来发表第一条吧！</p>
                    )}
                </>
            )}
        </div>
    );
};

export default MessageList;