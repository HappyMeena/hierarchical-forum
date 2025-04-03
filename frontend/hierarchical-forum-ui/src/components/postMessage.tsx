import React, { useState } from 'react';
import { postMessage } from '../api';

interface PostMessageProps {
    parentId?: number;
    onSuccess: () => void; 
    onCancel?: () => void;
}

const PostMessage: React.FC<PostMessageProps> = ({ parentId, onSuccess, onCancel }) => {
    const [content, setContent] = useState('');
    const [remainingChars, setRemainingChars] = useState(200);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleContentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        const newContent = e.target.value;
        setContent(newContent);
        setRemainingChars(200 - newContent.length);
    };

    const handlePostMessage = async () => {
        if (content.length < 3 || content.length > 200) {
            alert('留言长度必须在3到200字之间');
            return;
        }

        setIsSubmitting(true);
        try {
            await postMessage(content, parentId);
            setContent('');
            setRemainingChars(200);
            onSuccess(); 
        } catch (error) {
            console.error('发表留言失败', error);
            alert('发表留言失败，请检查是否已登录');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div style={{ 
            marginBottom: '20px',
            padding: '15px',
            border: '1px solid #eee',
            borderRadius: '4px',
            backgroundColor: parentId ? '#f5f5f5' : '#fafafa'
        }}>
            <h3 style={{ marginTop: 0 }}>{parentId ? '发表回复' : '发表留言'}</h3>
            <textarea
                value={content}
                onChange={handleContentChange}
                placeholder={parentId ? '请输入回复内容' : '请输入留言内容'}
                rows={4}
                style={{ 
                    width: '100%', 
                    padding: '10px',
                    border: '1px solid #ddd',
                    borderRadius: '4px',
                    marginBottom: '10px'
                }}
            />
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <p style={{ margin: 0, color: '#666' }}>还可以输入 {remainingChars} 字</p>
                <div>
                    {parentId && onCancel && (
                        <button
                            onClick={onCancel}
                            style={{ 
                                padding: '8px 16px', 
                                background: '#f0f0f0', 
                                color: '#333', 
                                border: '1px solid #ddd', 
                                borderRadius: '4px',
                                cursor: 'pointer',
                                marginRight: '10px'
                            }}
                        >
                            取消
                        </button>
                    )}
                    <button
                        onClick={handlePostMessage}
                        disabled={isSubmitting}
                        style={{ 
                            padding: '8px 16px', 
                            background: isSubmitting ? '#cccccc' : '#007bff', 
                            color: 'white', 
                            border: 'none', 
                            borderRadius: '4px',
                            cursor: isSubmitting ? 'not-allowed' : 'pointer'
                        }}
                    >
                        {isSubmitting ? '提交中...' : parentId ? '发表回复' : '发表留言'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default PostMessage;